/*
 * This file is part of PES Editor.
 *
 * PES Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PES Editor is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PES Editor.  If not, see <http://www.gnu.org/licenses/>.
 */
package editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class PSDStatPaste extends JDialog 
{
	private static boolean debug = false;
	
	// JPanel
	JLabel lbl = new JLabel("Paste PSD stat here:");
	JPanel pnl = new JPanel();
	// Buttons
	JButton btn = new JButton("Import PSD Stat");
		
	JTextArea stat = new JTextArea("", 5, 20);
	JScrollPane sp = new JScrollPane(stat);
	PlayerDialog retForm;

	public static final String[] nationalities = { "Austrian", "Belgian", "Bulgarian", "Croatian", "Czech", "Danish", "English", "Finnish", "French", "German", "Greek", "Hungarian", "Irish", "Italian", "Latvia", "Dutch", "Northern Irish", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian", "Scottish", "Serbian", "Slovak", "Slovenian", "Spanish", "Swedish", "Swiss", "Turkish", "Ukrainian", "Welsh", "Angola", "Cameroonian", "Ivorian", "Ghanaian", "Nigerian", "South Africa", "Togo", "Tunisia", "Costa Rican", "Mexican", "Trinidadian", "American", "Argentinian", "Brazilian", "Chilean", "Colombian", "Ecuadorian", "Paraguayan", "Peruvian", "Uruguayan", "Iran", "Japanese", "Saudi Arabia", "South Korean", "Australian", "Bosnian", "Estonia", "Israeli", "Honduran", "Jamaican", "Panama", "Bolivian", "Venezuelan", "China", "Uzbekistan", "Albanian", "Cyprus", "Icelandic", "Macedonian", "Armenia", "Belarusian", "Georgian", "Liechtenstein", "Lithuania", "Algerian", "Benin", "Burkina Faso", "Cape Verde", "Congo", "DR Congo", "Egypt", "Equatorial Guinea", "Gabon", "Gambia", "Guinea", "Guinea-Bissau", "Kenya", "Liberia", "Libya", "Mali", "Moroccan", "Mozambique", "Senegalese", "Sierra Leone", "Zambia", "Zimbabwean", "Canadian", "Grenada", "Guadeloupe", "Martinique", "Netherlands Antilles", "Oman", "New Zealand", "Free Nationality" };

	public static final String[][] positionNames = 
	{
		{ "GK", "?", "CWP", "CBT", "SB", "DM",  "WB", "CM",  "SM",  "AM",  "WG", "SS", "CF"},
		{ "GK", "?", "CWP", "CB",  "SB", "DMF", "WB", "CMF", "SMF", "AMF", "WF", "SS", "CF"},
	};
	public static final String[][] statNames = 
	{
		{ "Attack", "Defence", "Balance", "Stamina", "Speed", "Acceleration", "Response", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Shot Power", "Shot Technique", "Free Kick Accuracy", "Swerve", "Heading", "Jump", "Technique", "Aggression", "Mentality", "GK Skills", "Team Work" }, //ORIGINAL PES STAT NAMES

		{ "Attack", "Defence", "Balance", "Stamina", "Top Speed", "Acceleration", "Response", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Shot Power", "Shot Technique", "Free Kick Accuracy", "Curling", "Header", "Jump", "Technique", "Aggression", "Mentality", "Keeper Skills", "Teamwork" }, //PSD PES6

		{ "Attacking Prowess", "Defence Prowess", "Body Balance", "Stamina", "Speed", "Explosive Power", "Response", "Agility", "Dribbling", "Dribble Speed", "Low Pass", "Short Pass Speed", "Lofted Pass", "Long Pass Speed", "Finishing", "Kicking Power", "Shot Technique", "Place Kicking", "Controlled Spin", "Header", "Jump", "Ball Control", "Aggression", "Tenacity", "Goalkeeping", "Teamwork" }, //PSD PES14

		{ "Attack", "Defence", "Balance", "Stamina", "Top Speed", "Explosive Power", "Reflexes", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Kicking Power", "Shot Technique", "Place Kicking", "Curling", "Header Accuracy", "Jump", "Ball Controll", "Aggression", "Tenacity", "Goal Keeping Skills", "Teamwork" } //PSD PES18
		
	};

	public static String[] abilityNames = { "Dribbling", "Tactical dribble", "Positioning", "Reaction", "Playmaking", "Passing", "Scoring", "1-1 Scoring", "Post player", "Lines", "Middle shooting", "Side", "Centre", "Penalties", "1-Touch pass", "Outside", "Marking", "Sliding", "Covering", "D-Line control", "Penalty stopper", "1-On-1 stopper", "Long throw" };


	public static boolean strIn(String a, String b) { return a.toLowerCase().contains(b.toLowerCase()); }
	
	private static void debugWrite(String s)
	{
		if (debug)
			System.out.println(s);
	}
	static class SelectAll extends TextAction
	{
		public SelectAll()
		{
		    super("Select All");
		    putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		}

		public void actionPerformed(ActionEvent e)
		{
		    JTextComponent component = getFocusedComponent();
		    component.selectAll();
		    component.requestFocusInWindow();
		}
	}
	private void sendPSD(String s)
	{
		String lines[] = s.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
		String side="";
		String foot="";
		// CLEAR ALL STATS
		for (int z=0; z<abilityNames.length; z++)
			retForm.spePanel.checkBox[z].setSelected(false);
		for (int z=0; z<statNames[0].length; z++)
			retForm.abiPanel.field[z].setText("0");
		for (int i = 0; i < Stats.roles.length; i++)
			retForm.posPanel.checkBox[i].setSelected(false);
		for (int i = 0; i < abilityNames.length; i++)
			retForm.spePanel.checkBox[i].setSelected(false);

		retForm.posPanel.updateRegBox();
		
		retForm.genPanel.nationBox.setSelectedItem(nationalities.length-1);
		retForm.genPanel.wfaBox.setSelectedIndex(0);
		retForm.genPanel.wffBox.setSelectedIndex(0);
		retForm.genPanel.condBox.setSelectedIndex(0);
		//retForm.genPanel.consBox.setSelectedIndex(0); //requested by Buggy
		retForm.genPanel.injuryBox.setSelectedIndex(0);
		retForm.genPanel.dribBox.setSelectedIndex(0);
		retForm.genPanel.fkBox.setSelectedIndex(0);
		retForm.genPanel.pkBox.setSelectedIndex(0);
		retForm.genPanel.dkBox.setSelectedIndex(0);
		retForm.genPanel.footBox.setSelectedIndex(0);
		retForm.genPanel.ageField.setText("0");
		retForm.genPanel.heightField.setText("0");
		retForm.genPanel.weightField.setText("0");
		
		//START PARSING PSD STATS

		for (int i=0; i<lines.length; i++)	
		{
			try
			{
				String parts[] = lines[i].split(":");
				String f = parts[0];
				
				
				if (parts.length>1) //ABILITY99!!
				{
					
					if (strIn("name",f))
					{
						debugWrite("Found name >"+parts[1]);
						continue;
					}
					if (strIn(f,"Shirt Name"))
					{
						debugWrite("Found shirt name >"+parts[1]);
						continue;
					}
					
					if (strIn(f,"Positions"))
					{
						debugWrite("Found Positions! ");
						String[] posList = parts[1].split(",");
						for (int z=0; z<posList.length; z++)
						{
							String tmp = posList[z].replace("★","").replace("*","").trim().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");
							boolean getOut = false;
							for (int k = 0; k < positionNames.length && !getOut; k++)
								for (int k1 = 0; k1<positionNames[k].length && !getOut; k1++)
 									if (tmp.toUpperCase().equals(positionNames[k][k1].toUpperCase()))
									{
										getOut = true;
										tmp = positionNames[0][k1];
										retForm.posPanel.checkBox[k1].setSelected(true);
									}
							 if (strIn(posList[z],"★") || strIn(posList[z],"*")) 
							{
								debugWrite("Reg:"+tmp+" ");
								retForm.posPanel.updateRegBox();
								retForm.posPanel.regBox.setSelectedItem(tmp);
							}
							else
								debugWrite(tmp+" ");
						}
						debugWrite("");
						continue;
					}

					if (strIn("Side",f))
					{
						side = parts[1].toUpperCase().trim();
						debugWrite("Found side >"+side);
						continue;
					}
					if (strIn("Foot",f))
					{
						foot = parts[1].toUpperCase().trim();
						debugWrite("Found foot >"+foot);
						continue;
					}
					if (strIn(f,"Nationality"))
					{
						String nation = parts[1].trim().replace(" ","");
						debugWrite("Found Nationality >\""+nation+"\"");
						int index = nationalities.length-1;
						for (int k=0; k<nationalities.length; k++)
							if (nation.toUpperCase().equals(nationalities[k].toUpperCase()))
								{ index = k; break;}
						String normalNation = Stats.nation[index];
						retForm.genPanel.nationBox.setSelectedItem(normalNation);				
						continue;
					}
					if (strIn(f,"Age"))
					{
						String tmp = parts[1].replace("("," ").replace(" "," ");
						debugWrite("Age string>"+tmp+"<");
						tmp = tmp.trim().split(" ")[0];
						tmp = tmp.trim().replace(" ","");
						debugWrite("Age stringint>"+tmp);
						int age = Integer.parseInt(tmp);
						debugWrite("Found age > "+age);
						retForm.genPanel.ageField.setText(Integer.toString(age));
						continue;
					}
					if (strIn(f,"Height"))
					{
						int height = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Height > "+height);
						retForm.genPanel.heightField.setText(Integer.toString(height));
						continue;
					}
					if (strIn(f,"Weight"))
					{
						int weight = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Weight > "+weight);
						retForm.genPanel.weightField.setText(Integer.toString(weight));
						continue;
					}
					if (strIn(f,"Weak Foot Accuracy"))
					{
						int wfa = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Weak Foot Accuracy > "+wfa);
						retForm.genPanel.wfaBox.setSelectedItem(Integer.toString(wfa));
						continue;
					}
					if (strIn(f,"Weak Foot Frequency"))
					{
						int wff = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Weak Foot Frequency > "+wff);
						retForm.genPanel.wffBox.setSelectedItem(Integer.toString(wff));
						continue;
					}
					if (strIn(f,"Condition") || strIn(f,"Fitness"))
					{
						int con = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Condition > "+con);
						retForm.genPanel.condBox.setSelectedItem(Integer.toString(con));
						continue;
					}
					if (strIn(f,"Consistency"))
					{
						int con = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Consistency > "+con);
						retForm.genPanel.consBox.setSelectedItem(Integer.toString(con));
						continue;
					}
					if (strIn(f,"Injury"))
					{
						String inj = parts[1].toUpperCase().trim().substring(0,1);
						debugWrite("Found Injury Tolerance >"+inj);
						retForm.genPanel.injuryBox.setSelectedItem(inj);
						continue;
					}
					if (strIn(f,"Dribble Style"))
					{
						int ds = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Dribble Style >"+ds);
						retForm.genPanel.dribBox.setSelectedItem(Integer.toString(ds));
						continue;
					}
					if (strIn(f,"Free Kick Style"))
					{
						int fks = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Dribble Style >"+fks);
						retForm.genPanel.fkBox.setSelectedItem(Integer.toString(fks));
						continue;
					}
					if (strIn(f,"Penalty Style"))
					{
						int ps = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Penalty Style >"+ps);
						retForm.genPanel.pkBox.setSelectedItem(Integer.toString(ps));
						continue;
					}
					if (strIn(f,"Drop Kick Style"))
					{
						int dk = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found Drop Kick Style >"+dk);
						retForm.genPanel.dkBox.setSelectedItem(Integer.toString(dk));
						continue;
					}
					boolean foundStat = false;
					int statIndex = -1;

					for (int j=0; ((!foundStat) && (j<statNames.length)); j++)
						for (int z=0; ((!foundStat) && (z<statNames[j].length)); z++)
							if(strIn(statNames[j][z],f) && strIn(f,statNames[j][z]))
								{ foundStat=true; statIndex = z;}
					
					if (foundStat)
					{
						int statt = Integer.parseInt(parts[1].trim().split(" ")[0].replaceAll("[\\D]", ""));
						debugWrite("Found stat: "+statNames[0][statIndex]+"="+f+" value -> "+statt);
						retForm.abiPanel.field[statIndex].setText(Integer.toString(statt));
					}
					else
						debugWrite("Nope. "+f);
				}
				else
				{
					f = f.replace("★","").replace("*","").trim();
					for (int z=0; z<abilityNames.length; z++)
						if (strIn(abilityNames[z],f) && strIn(f,abilityNames[z]))
						{
							debugWrite("Found special "+abilityNames[z]);
							retForm.spePanel.checkBox[z].setSelected(true);	
							break;
						}
				}
				debugWrite("");
			}
			catch(Exception e){ debugWrite("Error! "+e);}
		}
		String fs = foot+" foot / "+side+" side";
		debugWrite("Foot/side combo:"+fs);
		retForm.genPanel.footBox.setSelectedItem(fs);
	}

	public PSDStatPaste(Frame owner, PlayerDialog ret) 
	{
		super(owner, "Paste PSD Stat", true);
		retForm = ret;
		pnl.setLayout(null);
		
		JPopupMenu menu = new JPopupMenu();
		Action cut = new DefaultEditorKit.CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		menu.add( cut );

		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		menu.add( copy );

		Action paste = new DefaultEditorKit.PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		menu.add( paste );

		Action selectAll = new SelectAll();
		menu.add( selectAll );
		stat.setComponentPopupMenu( menu );


		lbl.setBounds(0, 0, 400, 30);
		pnl.add(lbl);

		sp.setBounds(0, 30, 400, 340);
		pnl.add(sp);
		btn.setBounds(0, 370, 400, 30);

		btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				sendPSD(stat.getText());
			}
		});

		// JPanel bounds
		pnl.setBounds(0, 0, 400, 400);

		// Adding to JFrame
		pnl.add(btn);
		add(pnl);

		// JFrame properties
		setSize(400, 430);
		setResizable(false);
		setBackground(Color.BLACK);
		setTitle("PSD Stats Paste");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
