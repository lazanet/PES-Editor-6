/*
 * Copyright 2008-9 Compulsion
 * <pes_compulsion@yahoo.co.uk>
 * <http://www.purplehaze.eclipse.co.uk/>
 * <http://uk.geocities.com/pes_compulsion/>
 *
 * This file is part of PES Editor.
 *
 * PES Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PES Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PES Editor.  If not, see <http://www.gnu.org/licenses/>.
 */

package editor;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class Ability99Panel extends JPanel implements ActionListener,CaretListener, KeyListener
{
	OptionFile of;
	// Stats stats;
	int player;

	JTextField[] field;

	 String[] ability99 = { "Attack", "Defence", "Balance", "Stamina", "Speed", "Acceleration", "Response", "Agility", "Dribble Accuracy", "Dribble Speed", "Short Pass Accuracy", "Short Pass Speed", "Long Pass Accuracy", "Long Pass Speed", "Shot Accuracy", "Shot Power", "Shot Technique", "Free Kick Accuracy", "Swerve", "Heading", "Jump", "Technique", "Aggression", "Mentality", "GK Skills", "Team Work" };

	String[] initVal;

	int f;

	public Ability99Panel(OptionFile opf) {
		super(new GridBagLayout());
		of = opf;
		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 2;
		// stats = s;
		setBorder(BorderFactory.createTitledBorder("1-99 Ability"));
		field = new JTextField[ability99.length];
		initVal = new String[ability99.length];
		Verifier99 verifier99 = new Verifier99();
		for (int i = 0; i < ability99.length; i++)
		{
			field[i] = new JTextField(2);
			// JLabel lab = new JLabel(ability99[i]);
			// lab.setHorizontalAlignment(SwingConstants.TRAILING);

			c.anchor = GridBagConstraints.EAST;
			c.gridx = 0;
			c.gridy = i;
			add(new JLabel(ability99[i]), c);

			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 1;
			c.gridy = i;
			add(field[i], c);

			// add(lab);
			// add(field[i]);
			// System.out.println(spinner[i].getEditor());
			field[i].setActionCommand(String.valueOf(i));
			field[i].addActionListener(this);
			field[i].setInputVerifier(verifier99);
			field[i].addCaretListener(this);
			field[i].addKeyListener(this);
		}
	}

	public void load(int p)
	{
		player = p;
		for (int i = 0; i < Stats.ability99.length; i++)
		{
			initVal[i] = Stats.getString(of, player, Stats.ability99[i]);
			field[i].setText(initVal[i]);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		f = new Integer(e.getActionCommand()).intValue();

		try
		{
			int v = new Integer(((JTextField) e.getSource()).getText()).intValue();
			// System.out.println(f);
			if (v > 0 && v < 100) 
			{
				if (f < ability99.length - 1)
				{
					field[f + 1].requestFocus();
					field[f + 1].selectAll();
				}
				else 
				{
					field[0].requestFocus();
					field[0].selectAll();
				}
			}
			else
			{
				field[f].setText(initVal[f]);
				field[f].selectAll();
			}
		}
		catch (NumberFormatException nfe)
		{
			field[f].setText(initVal[f]);
			field[f].selectAll();
		}
	}

	class Verifier99 extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			boolean ok = false;
			JTextField tf = (JTextField) input;
			try
			{
				int v = new Integer(tf.getText()).intValue();
				if (v > 0 && v < 100)
					ok = true;
			} 
			catch (NumberFormatException nfe)
			{
				ok = false;
			}
			return ok;
		}
	}

	public void caretUpdate(CaretEvent ce)
	{
		JTextField tf = (JTextField) ce.getSource();
		String text = tf.getText();
		if (text != "")
		{
			try
			{
				int v = new Integer(((JTextField) ce.getSource()).getText()).intValue();
				if (v >= 75 && v < 80)
					tf.setBackground(new Color(183, 255, 0));
				else if (v >= 80 && v < 90)
					tf.setBackground(Color.yellow);
				else if (v >= 90 && v < 95)
					tf.setBackground(Color.orange);
				else if (v >= 80 && v < 100)
					tf.setBackground(Color.red);
				else
					tf.setBackground(Color.white);
			}
			catch (NumberFormatException nfe)
			{
				tf.setBackground(Color.white);
			}
		} 
		else
			tf.setBackground(Color.white);
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e)
	{
		JTextField tf = (JTextField) e.getSource();
		int v = new Integer(tf.getText()).intValue();

		int key = e.getKeyCode();
		if (key == 38 && v < 99)
		{
			v++;
			tf.setText(String.valueOf(v));
		}
		if (key == 40 && v > 1)
		{
			v--;
			tf.setText(String.valueOf(v));
		}
	}

	public void keyReleased(KeyEvent e) {}
}
