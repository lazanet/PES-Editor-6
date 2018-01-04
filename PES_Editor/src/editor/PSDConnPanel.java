package editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;

public class PSDConnPanel extends JDialog
{
	JTextField teamSearch;
	JLabel teamSearchLbl;
	JComboBox teamSelection;
	JButton teamSearchButton;
	JList<String> playerList;
	JLabel playerSearchCaption;
	JTextField playerSearch;
	JButton playerSearchButton;
	JTextArea statData;
	JLabel statLbl;

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

	JScrollPane sp1 = new JScrollPane(statData);
	JScrollPane sp2 = new JScrollPane(playerList);

	DefaultListModel<String> playerListModel;

	final String domain = "https://pesstatsdatabasedapi.herokuapp.com";
	
	public static String getHTML(String urlToRead)
	{
		try
		{
			StringBuilder result = new StringBuilder();
			URL url = new URL(urlToRead);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line;
			while ((line = rd.readLine())!= null)
				result.append(line+"\n");
		    
			rd.close();
			return result.toString();
		}
		catch(Exception e)
		{
			System.out.println("Internet error: "+e);
		}
		return "";
   	}

	public PSDConnPanel()
	{
		setTitle("PesStatsDatabase");
		setLayout(null);
		setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		setModalityType(Dialog.ModalityType.MODELESS);
		setupGUI();
	}
	void setupGUI()
	{
		teamSearch = new JTextField();
		teamSearch.setLocation(9,35);
		teamSearch.setSize(158,28);
		teamSearch.setBackground(new Color(-1));
		teamSearch.setText("");
		teamSearch.setColumns(10);
		add(teamSearch);

		teamSearchLbl = new JLabel();
		teamSearchLbl.setLocation(6,6);
		teamSearchLbl.setSize(153,25);
		teamSearchLbl.setText("Team name search:");
		add(teamSearchLbl);

		teamSelection = new JComboBox();
		teamSelection.setLocation(9,78);
		teamSelection.setSize(253,30);
		teamSelection.setEditable(false);
		add(teamSelection);

		teamSelection.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playerListModel.clear();
				String teamDesc = (String)teamSelection.getSelectedItem();
				String teamNo = teamDesc.split(";")[0];
				if (teamNo=="") return;
				String res = getHTML(domain+"/index.php?t="+teamNo.trim());
				String lines[] = res.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
				for (int i=0; i<lines.length; i++)
					playerListModel.addElement(lines[i]);
				
			}
		});

		teamSearchButton = new JButton();
		teamSearchButton.setLocation(176,33);
		teamSearchButton.setSize(85,33);
		teamSearchButton.setText("Search");
		add(teamSearchButton);

		teamSearchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				teamSelection.removeAllItems();
				String teamName = teamSearch.getText();
				if (teamName=="") return;
				String res = getHTML(domain+"/index.php?n="+teamName.trim());
				String lines[] = res.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
				for (int i=0; i<lines.length; i++)
					teamSelection.addItem(lines[i]);
				
			}
		});

		playerListModel = new DefaultListModel<>();
		playerList = new JList(playerListModel);
		playerList.setLocation(287,77);
		playerList.setSize(308,345);
		playerList.setBackground(new Color(-1));
		add(playerList);
		
		MouseListener playerListMouseListener = new MouseAdapter()
		{
    			public void mouseClicked(MouseEvent e)
			{
				try
				{
					String playerDesc = (String)playerList.getSelectedValue();
					if (playerDesc=="") return;
					String[] tmp = playerDesc.split(";");
					if (tmp.length<2) return;
					String playerId = tmp[1]; 
					String res = getHTML(domain+"/index.php?v=6&p="+playerId.trim());
					statData.setText(res);
				}
				catch(Exception exp)
				{
				}
    			}
		};
		
		playerList.addMouseListener(playerListMouseListener);


		playerSearchCaption = new JLabel();
		playerSearchCaption.setLocation(286,5);
		playerSearchCaption.setSize(116,26);
		playerSearchCaption.setText("Player search:");
		add(playerSearchCaption);

		playerSearch = new JTextField();
		playerSearch.setLocation(286,36);
		playerSearch.setSize(227,31);
		playerSearch.setBackground(new Color(-1));
		playerSearch.setText("");
		playerSearch.setColumns(10);
		add(playerSearch);
		

		playerSearchButton = new JButton();
		playerSearchButton.setLocation(520,36);
		playerSearchButton.setSize(85,33);
		playerSearchButton.setText("Search");
		add(playerSearchButton);

		playerSearchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playerListModel.clear();
				String playerName = playerSearch.getText();
				if (playerName=="") return;
				String res = getHTML(domain+"/index.php?s="+playerName.trim());
				String lines[] = res.replaceAll("(?m)^[ \t]*\r?\n", "").split("\\r?\\n");
				for (int i=0; i<lines.length; i++)
					playerListModel.addElement(lines[i]);
				
			}
		});

		statData = new JTextArea();
		statData.setLocation(11,160);
		statData.setSize(257,259);
		statData.setText("");
		add(statData);

		statLbl = new JLabel();
		statLbl.setLocation(9,123);
		statLbl.setSize(225,25);
		statLbl.setText("Stat:");
		add(statLbl);

		JPopupMenu menu = new JPopupMenu();
		Action cut = new DefaultEditorKit.CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		menu.add(cut);

		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		menu.add(copy);

		Action paste = new DefaultEditorKit.PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		menu.add(paste);

		Action selectAll = new SelectAll();
		menu.add(selectAll);

		statData.setComponentPopupMenu(menu);
		playerSearch.setComponentPopupMenu(menu);
		teamSearch.setComponentPopupMenu(menu);
		add(sp1);
		add(sp2);

		setSize(618,476);
		setVisible(true);
		
	}
} 
