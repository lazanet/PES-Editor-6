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
 * PES Editor is distributed in the hope that it will be useful, * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PES Editor.  If not, see <http://www.gnu.org/licenses/>.
 */
package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StadiumPanel extends JPanel implements ActionListener,
		ListSelectionListener {
	private OptionFile of;

	// JLabel[] name = new JLabel[30];
	private JTextField editor;

	private JList list;

	private TeamPanel teamPanel;

	public StadiumPanel(OptionFile opf, TeamPanel tp) {
		super();
		of = opf;
		teamPanel = tp;
		init();
		// refresh();
	}

	public void init() {
		editor = new JTextField(15);
		editor.setToolTipText("Enter new name and press return");
		editor.addActionListener(this);
		list = new JList();
		list.addListSelectionListener(this);
		list.setVisibleRowCount(30);
		JScrollPane scroll = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setViewportView(list);
		JPanel pan = new JPanel();
		pan.setBorder(BorderFactory.createTitledBorder("Stadium Names"));
		pan.add(scroll);
		pan.add(editor);
		add(pan);
	}

	public void refresh() {
		list.setListData(Stadia.get(of));
	}

	public void actionPerformed(ActionEvent evt) {
		// JTextField source = evt.getSource();
		int sn = list.getSelectedIndex();
		String text = editor.getText();
		if (sn != -1 && text.length() < Stadia.maxLen && text.length() > 0) {
			if (!(text.equals(Stadia.get(of, sn)))) {
				Stadia.set(of, sn, text);
				teamPanel.refresh();
				refresh();
			}
			if (sn < Stadia.total - 1) {
				list.setSelectedIndex(sn + 1);
				list.ensureIndexIsVisible(list.getSelectedIndex());
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			int i = list.getSelectedIndex();
			if (i == -1) {
				editor.setText("");
			} else {
				editor.setText(Stadia.get(of, i));
				editor.selectAll();
			}
		}
	}

}
