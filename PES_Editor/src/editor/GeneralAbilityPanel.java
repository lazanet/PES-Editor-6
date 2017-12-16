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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GeneralAbilityPanel extends JPanel
{
	OptionFile of ;
	int player;

	JComboBox nationBox;

	JTextField ageField;

	JTextField heightField;

	JTextField weightField;

	JComboBox footBox;

	JComboBox wfaBox;

	JComboBox wffBox;

	JComboBox consBox;

	JComboBox condBox;

	JComboBox injuryBox;

	JComboBox fkBox;

	JComboBox pkBox;

	JComboBox dribBox;

	JComboBox dkBox;

	public GeneralAbilityPanel(OptionFile opf)
	{
		super(new GridLayout(0, 2)); of = opf;
		setBorder(BorderFactory.createTitledBorder("General"));
		// stats = s;
		nationBox = new JComboBox(Stats.nation);
		ageField = new JTextField(2);
		ageField.setInputVerifier(new VerifierAge());
		heightField = new JTextField(2);
		heightField.setInputVerifier(new VerifierHeight());
		weightField = new JTextField(2);
		weightField.setInputVerifier(new VerifierWeight());
		String[] modF = {
			"R foot / R side",
			"R foot / L side",
			"R foot / B side",
			"L foot / L side",
			"L foot / R side",
			"L foot / B side"
		};
		footBox = new JComboBox(modF);
		wfaBox = new JComboBox(Stats.mod18);
		wffBox = new JComboBox(Stats.mod18);
		consBox = new JComboBox(Stats.mod18);
		condBox = new JComboBox(Stats.mod18);
		injuryBox = new JComboBox(Stats.modInjury);
		String[] mod14 = {
			"1",
			"2",
			"3",
			"4"
		};
		dribBox = new JComboBox(mod14);
		dkBox = new JComboBox(mod14);
		String[] mod19 = {
			"1",
			"2",
			"3",
			"4",
			"5",
			"6",
			"7",
			"8",
			"9",
			"10"
		};
		fkBox = new JComboBox(mod19);
		String[] mod15 = {
			"1",
			"2",
			"3",
			"4",
			"5"
		};
		pkBox = new JComboBox(mod15);

		add(new JLabel("Nationality"));
		add(nationBox);
		add(new JLabel("Age"));
		add(ageField);
		add(new JLabel("Height"));
		add(heightField);
		add(new JLabel("Weight"));
		add(weightField);
		add(new JLabel("Foot / Side"));
		add(footBox);
		add(new JLabel("Weak Foot Accuracy"));
		add(wfaBox);
		add(new JLabel("Weak Foot Frequency"));
		add(wffBox);
		add(new JLabel("Consistency"));
		add(consBox);
		add(new JLabel("Condition"));
		add(condBox);
		add(new JLabel("Injury Tolerancy"));
		add(injuryBox);
		add(new JLabel("Dribble Style"));
		add(dribBox);
		add(new JLabel("Free Kick Style"));
		add(fkBox);
		add(new JLabel("Penalty Style"));
		add(pkBox);
		add(new JLabel("Drop Kick Style"));
		add(dkBox);
	}

	public void load(int p)
	{
		player = p;
		nationBox.setSelectedItem(Stats
			.getString( of , player, Stats.nationality));
		ageField.setText(Stats.getString( of , player, Stats.age));
		heightField.setText(Stats.getString( of , player, Stats.height));
		weightField.setText(Stats.getString( of , player, Stats.weight));

		wfaBox.setSelectedItem(Stats.getString( of , player, Stats.wfa));
		wffBox.setSelectedItem(Stats.getString( of , player, Stats.wff));
		consBox.setSelectedItem(Stats.getString( of , player, Stats.consistency));
		condBox.setSelectedItem(Stats.getString( of , player, Stats.condition));
		injuryBox.setSelectedItem(Stats.getString( of , player, Stats.injury));
		fkBox.setSelectedItem(Stats.getString( of , player, Stats.freekick));
		pkBox.setSelectedItem(Stats.getString( of , player, Stats.pkStyle));
		dribBox.setSelectedItem(Stats.getString( of , player, Stats.dribSty));
		dkBox.setSelectedItem(Stats.getString( of , player, Stats.dkSty));

		int foot = Stats.getValue( of , player, Stats.foot);
		int side = Stats.getValue( of , player, Stats.favSide);
		int item = (foot * 3) + side;
		footBox.setSelectedIndex(item);

	}

	class VerifierHeight extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			boolean ok = false;
			JTextField tf = (JTextField) input;
			try
			{
				int v = new Integer(tf.getText()).intValue();
				if (v >= 148 && v <= 211)
				{
					ok = true;
				}
			}
			catch (NumberFormatException nfe)
			{
				ok = false;
			}
			return ok;
		}
	}

	class VerifierWeight extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			boolean ok = false;
			JTextField tf = (JTextField) input;
			try
			{
				int v = new Integer(tf.getText()).intValue();
				if (v >= 1 && v < 128)
				{
					ok = true;
				}
			}
			catch (NumberFormatException nfe)
			{
				ok = false;
			}
			return ok;
		}
	}

	class VerifierAge extends InputVerifier
	{
		public boolean verify(JComponent input)
		{
			boolean ok = false;
			JTextField tf = (JTextField) input;
			try
			{
				int v = new Integer(tf.getText()).intValue();
				if (v >= 15 && v <= 46)
				{
					ok = true;
				}
			}
			catch (NumberFormatException nfe)
			{
				ok = false;
			}
			return ok;
		}
	}

}
