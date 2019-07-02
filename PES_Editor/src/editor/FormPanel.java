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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FormPanel extends JPanel implements ListSelectionListener,
		DropTargetListener, DragSourceListener, DragGestureListener {
	int team;

	OptionFile of;

	SquadList squadList;

	PositionList posList;

	JobList sFK;

	JobList lFK;

	JobList rCR;

	JobList lCR;

	JobList pk;

	JobList cap;

	// byte[] original = new byte[31];
	
	private static final byte[] formData = { 9, 63, 9, 41, 11, 85, 11, 19, 18, 52, 26, 75,
			26, 29, 34, 64, 34, 40, 43, 52, 0, 7, 1, 9, 8, 12, 23, 22, 28, 24,
			38, 9, 63, 9, 41, 11, 85, 11, 19, 18, 61, 18, 43, 26, 75, 26, 29,
			34, 52, 43, 52, 0, 7, 1, 9, 8, 14, 10, 23, 22, 26, 38, 9, 63, 9,
			41, 11, 85, 11, 19, 18, 52, 26, 75, 26, 29, 34, 52, 43, 66, 43, 38,
			0, 7, 1, 9, 8, 12, 23, 22, 26, 40, 36, 9, 63, 9, 41, 11, 85, 11,
			19, 18, 61, 18, 43, 30, 70, 30, 34, 43, 66, 43, 38, 0, 7, 1, 9, 8,
			14, 10, 28, 24, 40, 36, 9, 63, 9, 41, 11, 85, 11, 19, 26, 77, 26,
			61, 26, 43, 26, 27, 43, 66, 43, 38, 0, 7, 1, 9, 8, 20, 21, 17, 18,
			40, 36, 9, 63, 9, 41, 11, 85, 11, 19, 18, 52, 30, 64, 30, 40, 43,
			72, 43, 32, 43, 52, 0, 7, 1, 9, 8, 12, 28, 24, 30, 29, 38, 9, 63,
			9, 41, 11, 85, 11, 19, 18, 61, 18, 43, 30, 52, 43, 72, 43, 32, 43,
			52, 0, 7, 1, 9, 8, 14, 10, 26, 30, 29, 38, 9, 63, 9, 41, 11, 85,
			11, 19, 26, 77, 26, 52, 26, 27, 43, 72, 43, 32, 43, 52, 0, 7, 1, 9,
			8, 21, 19, 17, 30, 29, 38, 9, 72, 9, 52, 9, 32, 18, 52, 26, 52, 26,
			77, 26, 27, 34, 64, 34, 40, 43, 52, 0, 7, 3, 1, 12, 19, 23, 22, 28,
			24, 38, 9, 72, 9, 52, 9, 32, 18, 61, 18, 43, 26, 77, 26, 27, 34,
			64, 34, 40, 43, 52, 0, 7, 3, 1, 14, 10, 23, 22, 28, 24, 38, 9, 72,
			9, 52, 9, 32, 18, 52, 26, 77, 26, 27, 34, 64, 34, 40, 43, 66, 43,
			38, 0, 7, 3, 1, 12, 23, 22, 28, 24, 40, 36, 9, 72, 9, 52, 9, 32,
			18, 61, 18, 43, 26, 77, 26, 27, 34, 52, 43, 66, 43, 38, 0, 7, 3, 1,
			14, 10, 23, 22, 26, 40, 36, 9, 72, 9, 52, 9, 32, 18, 52, 26, 77,
			26, 27, 34, 52, 43, 72, 43, 32, 43, 52, 0, 7, 3, 1, 12, 23, 22, 26,
			30, 29, 38, 9, 72, 9, 52, 9, 32, 18, 61, 18, 43, 30, 70, 30, 34,
			43, 72, 43, 32, 43, 52, 0, 7, 3, 1, 14, 10, 28, 24, 30, 29, 38, 9,
			72, 9, 52, 9, 32, 26, 77, 26, 61, 26, 43, 26, 27, 43, 72, 43, 32,
			43, 52, 0, 7, 3, 1, 20, 21, 17, 18, 30, 29, 38, 9, 72, 9, 52, 9,
			32, 12, 87, 12, 17, 18, 52, 26, 75, 26, 29, 34, 52, 43, 52, 0, 7,
			3, 1, 9, 8, 12, 23, 22, 26, 38, 9, 72, 9, 52, 9, 32, 12, 87, 12,
			17, 18, 61, 18, 43, 30, 70, 30, 34, 43, 52, 0, 7, 3, 1, 9, 8, 14,
			10, 28, 24, 38, 9, 72, 9, 52, 9, 32, 12, 87, 12, 17, 26, 77, 26,
			61, 26, 43, 26, 27, 43, 52, 0, 7, 3, 1, 9, 8, 20, 21, 17, 18, 38,
			9, 72, 9, 52, 9, 32, 12, 87, 12, 17, 18, 52, 34, 64, 34, 40, 43,
			66, 43, 38, 0, 7, 3, 1, 9, 8, 12, 28, 24, 40, 36, 9, 72, 9, 52, 9,
			32, 12, 87, 12, 17, 18, 61, 18, 43, 34, 52, 43, 66, 43, 38, 0, 7,
			3, 1, 9, 8, 14, 10, 26, 40, 36, 9, 72, 9, 52, 9, 32, 12, 87, 12,
			17, 26, 77, 26, 52, 26, 27, 43, 66, 43, 38, 0, 7, 3, 1, 9, 8, 21,
			19, 17, 40, 36 };
	
	/*private byte[] formData = { 9, 9, 11, 11, 18, 26, 26, 34, 34, 43, 63, 41,
			85, 19, 52, 75, 29, 64, 40, 52, 0, 7, 1, 9, 8, 12, 23, 22, 28, 24,
			38, 9, 9, 11, 11, 18, 18, 26, 26, 34, 43, 63, 41, 85, 19, 61, 43,
			75, 29, 52, 52, 0, 7, 1, 9, 8, 14, 10, 23, 22, 26, 38, 9, 9, 11,
			11, 18, 26, 26, 34, 43, 43, 63, 41, 85, 19, 52, 75, 29, 52, 66, 38,
			0, 7, 1, 9, 8, 12, 23, 22, 26, 40, 36, 9, 9, 11, 11, 18, 18, 30,
			30, 43, 43, 63, 41, 85, 19, 61, 43, 70, 34, 66, 38, 0, 7, 1, 9, 8,
			14, 10, 28, 24, 40, 36, 9, 9, 11, 11, 26, 26, 26, 26, 43, 43, 63,
			41, 85, 19, 77, 61, 43, 27, 66, 38, 0, 7, 1, 9, 8, 20, 21, 17, 18,
			40, 36, 9, 9, 11, 11, 18, 30, 30, 43, 43, 43, 63, 41, 85, 19, 52,
			64, 40, 72, 32, 52, 0, 7, 1, 9, 8, 12, 28, 24, 30, 29, 38, 9, 9,
			11, 11, 18, 18, 30, 43, 43, 43, 63, 41, 85, 19, 61, 43, 52, 72, 32,
			52, 0, 7, 1, 9, 8, 14, 10, 26, 30, 29, 38, 9, 9, 11, 11, 26, 26,
			26, 43, 43, 43, 63, 41, 85, 19, 77, 52, 27, 72, 32, 52, 0, 7, 1, 9,
			8, 21, 19, 17, 30, 29, 38, 9, 9, 9, 18, 26, 26, 26, 34, 34, 43, 72,
			52, 32, 52, 52, 77, 27, 64, 40, 52, 0, 7, 3, 1, 12, 19, 23, 22, 28,
			24, 38, 9, 9, 9, 18, 18, 26, 26, 34, 34, 43, 72, 52, 32, 61, 43,
			77, 27, 64, 40, 52, 0, 7, 3, 1, 14, 10, 23, 22, 28, 24, 38, 9, 9,
			9, 18, 26, 26, 34, 34, 43, 43, 72, 52, 32, 52, 77, 27, 64, 40, 66,
			38, 0, 7, 3, 1, 12, 23, 22, 28, 24, 40, 36, 9, 9, 9, 18, 18, 26,
			26, 34, 43, 43, 72, 52, 32, 61, 43, 77, 27, 52, 66, 38, 0, 7, 3, 1,
			14, 10, 23, 22, 26, 40, 36, 9, 9, 9, 18, 26, 26, 34, 43, 43, 43,
			72, 52, 32, 52, 77, 27, 52, 72, 32, 52, 0, 7, 3, 1, 12, 23, 22, 26,
			30, 29, 38, 9, 9, 9, 18, 18, 30, 30, 43, 43, 43, 72, 52, 32, 61,
			43, 70, 34, 72, 32, 52, 0, 7, 3, 1, 14, 10, 28, 24, 30, 29, 38, 9,
			9, 9, 26, 26, 26, 26, 43, 43, 43, 72, 52, 32, 77, 61, 43, 27, 72,
			32, 52, 0, 7, 3, 1, 20, 21, 17, 18, 30, 29, 38, 9, 9, 9, 12, 12,
			18, 26, 26, 34, 43, 72, 52, 32, 87, 17, 52, 75, 29, 52, 52, 0, 7,
			3, 1, 9, 8, 12, 23, 22, 26, 38, 9, 9, 9, 12, 12, 18, 18, 30, 30,
			43, 72, 52, 32, 87, 17, 61, 43, 70, 34, 52, 0, 7, 3, 1, 9, 8, 14,
			10, 28, 24, 38, 9, 9, 9, 12, 12, 26, 26, 26, 26, 43, 72, 52, 32,
			87, 17, 77, 61, 43, 27, 52, 0, 7, 3, 1, 9, 8, 20, 21, 17, 18, 38,
			9, 9, 9, 12, 12, 18, 34, 34, 43, 43, 72, 52, 32, 87, 17, 52, 64,
			40, 66, 38, 0, 7, 3, 1, 9, 8, 12, 28, 24, 40, 36, 9, 9, 9, 12, 12,
			18, 18, 34, 43, 43, 72, 52, 32, 87, 17, 61, 43, 52, 66, 38, 0, 7,
			3, 1, 9, 8, 14, 10, 26, 40, 36, 9, 9, 9, 12, 12, 26, 26, 26, 43,
			43, 72, 52, 32, 87, 17, 77, 52, 27, 66, 38, 0, 7, 3, 1, 9, 8, 21,
			19, 17, 40, 36 };*/

	private String[] formName = { "Formation", "4-5-1 A", "4-5-1 B", "4-4-2 A",
			"4-4-2 B", "4-4-2 C", "4-3-3 A", "4-3-3 B", "4-3-3 C", "3-6-1 A",
			"3-6-1 B", "3-5-2 A", "3-5-2 B", "3-4-3 A", "3-4-3 B", "3-4-3 C",
			"5-4-1 A", "5-4-1 B", "5-4-1 C", "5-3-2 A", "5-3-2 B", "5-3-2 C" };

	private JComboBox formBox;

	boolean ok = false;

	PitchPanel pitchPanel;

	static boolean fromPitch = false;

	private JButton snapButton;

	AttDefPanel adPanel;

	JComboBox roleBox;

	Role[] role;

	// JCheckBox roleCheck;
	// JCheckBox defAttCheck;
	// JCheckBox numCheck;
	JComboBox altBox;

	SquadNumList numList;

	private JFileChooser chooserPNG = new JFileChooser();

	private PNGFilter pngFilter = new PNGFilter();

	int def = 0;

	int mid = 0;

	int att = 0;

	private TeamSetPanel teamSetPan;

	private StrategyPanel stratPan;

	private DataFlavor localPlayerFlavor;

	private int sourceIndex = -1;

	public FormPanel(OptionFile opf) {
		super();
		of = opf;
		
		String localPlayerType = DataFlavor.javaJVMLocalObjectMimeType
				+ ";class=editor.Player";
		try {
			localPlayerFlavor = new DataFlavor(localPlayerType);
		} catch (ClassNotFoundException e) {
			System.out
					.println("FormTransferHandler: unable to create data flavor");
		}

		chooserPNG.addChoosableFileFilter(pngFilter);
		chooserPNG.setAcceptAllFileFilterUsed(false);
		chooserPNG.setDialogTitle("Save Snapshot");

		JPanel panel = new JPanel(new GridLayout(0, 6));
		JPanel panel2 = new JPanel(new BorderLayout());
		JPanel panel3 = new JPanel(new BorderLayout());
		JPanel panelR = new JPanel(new BorderLayout());

		numList = new SquadNumList(of);

		String[] items = { "Normal", "Strategy Plan A", "Strategy Plan B" };
		altBox = new JComboBox(items);
		altBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "y") {
					// countForm();
					posList.alt = altBox.getSelectedIndex();
					posList.refresh(team);
					// squadList.refresh(team);
					updateRoleBox();
					teamSetPan.alt = altBox.getSelectedIndex();
					teamSetPan.refresh(team);
					pitchPanel.repaint();
					adPanel.repaint();
				}
			}
		});

		/*
		 * ActionListener chkAct = new ActionListener() { public void
		 * actionPerformed (ActionEvent a) { if (roleCheck.isSelected()) {
		 * pitchPanel.roleOn = true; } else { pitchPanel.roleOn = false; } if
		 * (defAttCheck.isSelected()) { pitchPanel.defence = true;
		 * pitchPanel.attack = true; } else { pitchPanel.defence = false;
		 * pitchPanel.attack = false; } if (numCheck.isSelected()) {
		 * pitchPanel.numbers = true; } else { pitchPanel.numbers = false; }
		 * pitchPanel.repaint(); } }; roleCheck = new JCheckBox("Roles");
		 * roleCheck.setSelected(true); roleCheck.addActionListener(chkAct);
		 * defAttCheck = new JCheckBox("Att/Def");
		 * defAttCheck.setSelected(true); defAttCheck.addActionListener(chkAct);
		 * numCheck = new JCheckBox("Numbers"); numCheck.setSelected(true);
		 * numCheck.addActionListener(chkAct);
		 */

		roleBox = new JComboBox();
		roleBox.setPreferredSize(new Dimension(56, 25));
		roleBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "y") {
					int si = squadList.getSelectedIndex();
					Role role = (Role) roleBox.getSelectedItem();
					if (si >= 0 && si < 11 && role.index != -1) {
						//int a = 670641 + (628 * team) + 6232 + si;
						int oldRole = Formations.getPos(of, team, altBox.getSelectedIndex(), si);
						if (oldRole != role.index) {
							// System.out.println(oldRole);
							if (oldRole < 10) {
								if (role.index > 9) {
									if (role.index < 29) {
										Formations.setX(of, team, altBox.getSelectedIndex(), si, 25);
									} else {
										Formations.setX(of, team, altBox.getSelectedIndex(), si, 41);
									}
								}
							}
							if (oldRole > 9 && oldRole < 29) {
								if (role.index < 10) {
									Formations.setX(of, team, altBox.getSelectedIndex(), si, 8);
								} else if (role.index > 28) {
									Formations.setX(of, team, altBox.getSelectedIndex(), si, 41);
								}
							}
							if (oldRole > 28) {
								if (role.index < 29) {
									if (role.index < 10) {
										Formations.setX(of, team, altBox.getSelectedIndex(), si, 8);
									} else {
										Formations.setX(of, team, altBox.getSelectedIndex(), si, 25);
									}
								}
							}
							if (role.index == 8 || role.index == 15
									|| role.index == 22 || role.index == 29) {
								if (oldRole != 8 && oldRole != 15
										&& oldRole != 22 && oldRole != 29) {
									if (Formations.getY(of, team, altBox.getSelectedIndex(), si) > 50) {
										Formations.setY(of, team, altBox.getSelectedIndex(), si, 28);
									}
								}
							}
							if (role.index == 9 || role.index == 16
									|| role.index == 23 || role.index == 30) {
								if (oldRole != 9 && oldRole != 16
										&& oldRole != 23 && oldRole != 30) {
									if (Formations.getY(of, team, altBox.getSelectedIndex(), si) < 54) {
										Formations.setY(of, team, altBox.getSelectedIndex(), si, 76);
									}
								}
							}
						}
						Formations.setPos(of, team, altBox.getSelectedIndex(), si, role.index);
						//of.data[a + (altBox.getSelectedIndex() * 171)] = of
								//.toByte(role.index);
						// countForm();
						// System.out.println(oldRole);
						if (oldRole > 0 && oldRole < 8
								&& (role.index < 1 || role.index > 7)) {
							/*int swe = of.data[670785 + (628 * team) + 6232
									+ (altBox.getSelectedIndex() * 171)];
							// System.out.println(swe + ", " + si);
							if (si == swe) {
								// System.out.println("changing");
								of.data[670785 + (628 * team) + 6232
										+ (altBox.getSelectedIndex() * 171)] = 0;
								of.data[670784 + (628 * team) + 6232
										+ (altBox.getSelectedIndex() * 171)] = 0;
								
								 * for (int i = 1; swe == of.data[670785 + (628 *
								 * team) + 6232 + (altBox.getSelectedIndex() *
								 * 171)] && i < 11; i++) { String pos =
								 * (String)posList.getModel().getElementAt(i);
								 * if (pos.equals("CBT") || pos.equals("CBW") ||
								 * pos.equals("ASW")) { of.data[670785 + (628 *
								 * team) + 6232 + (altBox.getSelectedIndex() *
								 * 171)] = (byte)i; } }
								 
							}*/

							//swe = of.data[670612 + (628 * team) + 6232];
							if (altBox.getSelectedIndex() == 0 && si == Formations.getStratOlCB(of, team)) {
								//of.data[670612 + (628 * team) + 6232] = 0;
								Formations.setStratOlCB(of, team, 0);
								/*
								 * for (int s = 0; s < 4; s++) { int strat =
								 * of.data[670608 + s + (628 * team) + 6232]; if
								 * (strat == 7) { of.data[670608 + s + (628 *
								 * team) + 6232] = 0; } }
								 */
								/*
								 * for (int i = 1; swe == of.data[670612 + (628 *
								 * team) + 6232] && i < 11; i++) { byte pos =
								 * of.data[670641 + (628 * team) + 6232 + i]; if
								 * (pos > 0 && pos < 8) { of.data[670612 + (628 *
								 * team) + 6232] = (byte)i; } }
								 */
							}

						}

						updateRoleBox();

						posList.refresh(team);
						teamSetPan.refresh(team);
						stratPan.refresh(team);
						pitchPanel.repaint();
						adPanel.repaint();
					}
					// System.out.println(roleBox.getWidth());
					// System.out.println(roleBox.getHeight());
					// System.out.println(posList.getWidth());
					// System.out.println(posList.getHeight());
				}
			}
		});
		
		squadList = new SquadList(of, true);
		// squadList.setToolTipText("Right click to deselect player");
		squadList.addListSelectionListener(this);

		new DropTarget(squadList, this);
		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(squadList,
				DnDConstants.ACTION_MOVE, this);

		posList = new PositionList(of, false);
		teamSetPan = new TeamSetPanel(of);
		stratPan = new StrategyPanel(of, squadList, posList);

		adPanel = new AttDefPanel(of, altBox);
		pitchPanel = new PitchPanel(of, squadList, adPanel, altBox, numList);
		adPanel.pitch = pitchPanel;
		
		lFK = new JobList(of, 0, " F-L ", Color.yellow);
		lFK.setToolTipText("Long free kick");
		sFK = new JobList(of, 1, " F-S ", Color.yellow);
		sFK.setToolTipText("Short free kick");
		lCR = new JobList(of, 2, " C-L ", Color.cyan);
		lCR.setToolTipText("Left corner");
		rCR = new JobList(of, 3, " C-R", Color.cyan);
		rCR.setToolTipText("Right corner");
		pk = new JobList(of, 4, " PK ", Color.green);
		pk.setToolTipText("Penalty");
		cap = new JobList(of, 5, " C ", Color.red);
		cap.setToolTipText("Captain");
		formBox = new JComboBox();
		formBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = formBox.getSelectedIndex();
				// System.out.println(i);
				if (i != -1 && e.getActionCommand() == "y") {
					int a = Formations.startAdr + 118 + (Formations.size * team) + (altBox.getSelectedIndex() * Formations.abcSize);
					if (i != 0) {
						System.arraycopy(formData, (i - 1) * 31, of.data, a,
										31);
					}

					//byte swe = of.data[670785 + (628 * team) + 6232 + (altBox.getSelectedIndex() * 171)];
					//byte pos = Formations.getPos(of, team, altBox.getSelectedIndex(), swe);
					/*if (pos < 1 || pos > 7) {
						of.data[670785 + (628 * team) + 6232
								+ (altBox.getSelectedIndex() * 171)] = 0;
						of.data[670784 + (628 * team) + 6232
								+ (altBox.getSelectedIndex() * 171)] = 0;
						// System.out.println("changing");
						
						 * for (byte k = 1; swe == of.data[670785 + (628 * team) +
						 * 6232 + (altBox.getSelectedIndex() * 171)] && k < 11;
						 * k++) { String posS =
						 * (String)posList.getModel().getElementAt(k); if
						 * (posS.equals("CBT") || posS.equals("CBW") ||
						 * posS.equals("ASW")) { of.data[670785 + (628 * team) +
						 * 6232 + (altBox.getSelectedIndex() * 171)] = (byte)k; } }
						 
					}*/

					//swe = of.data[670612 + (628 * team) + 6232];
					byte pos = Formations.getPos(of, team, 0, Formations.getStratOlCB(of, team));
					if (altBox.getSelectedIndex() == 0 && (pos < 1 || pos > 7)) {
						//of.data[670612 + (628 * team) + 6232] = 0;
						Formations.setStratOlCB(of, team, 0);
						/*
						 * for (int s = 0; s < 4; s++) { int strat =
						 * of.data[670608 + s + (628 * team) + 6232]; if (strat ==
						 * 7) { of.data[670608 + s + (628 * team) + 6232] = 0; } }
						 */
						/*
						 * for (byte k = 1; swe == of.data[670612 + (628 * team) +
						 * 6232] && k < 11; k++) { pos = of.data[670641 + (628 *
						 * team) + 6232 + k]; if (pos > 0 && pos < 8) {
						 * of.data[670612 + (628 * team) + 6232] = (byte)k; } }
						 */
					}
					// countForm();
					posList.refresh(team);
					stratPan.refresh(team);
					teamSetPan.refresh(team);
					pitchPanel.repaint();
					adPanel.repaint();
					updateRoleBox();
				}
			}
		});
		// formBox.setEnabled(false);

		snapButton = new JButton("Snapshot");
		snapButton
				.setToolTipText("Save the formation diagram to a .png image file");
		snapButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				savePNG();
			}
		});

		// panel.add(posList);
		// panel.add(squadList);
		panel.add(lFK);
		panel.add(sFK);
		panel.add(lCR);
		panel.add(rCR);
		panel.add(pk);
		panel.add(cap);
		JPanel numPos = new JPanel(new BorderLayout());
		numPos.add(numList, BorderLayout.WEST);
		numPos.add(posList, BorderLayout.EAST);
		panel2.add(numPos, BorderLayout.WEST);
		panel2.add(squadList, BorderLayout.CENTER);
		panel2.add(panel, BorderLayout.EAST);
		panel3.add(panel2, BorderLayout.CENTER);
		panel3.add(formBox, BorderLayout.NORTH);
		
		JPanel setPanel = new JPanel();
		setPanel.add(adPanel);
		setPanel.add(roleBox);
		
		JPanel topPanel = new JPanel(new GridLayout(1, 3));
		topPanel.add(altBox);
		topPanel.add(formBox);
		topPanel.add(snapButton);
		
		JPanel botPan = new JPanel(new BorderLayout());
		botPan.add(teamSetPan, BorderLayout.NORTH);
		botPan.add(pitchPanel, BorderLayout.CENTER);
		botPan.add(setPanel, BorderLayout.SOUTH);


		panelR.add(topPanel, BorderLayout.NORTH);
		panelR.add(botPan, BorderLayout.CENTER);
		panelR.add(stratPan, BorderLayout.SOUTH);
		add(panel3);
		add(panelR);
	}

	public void refresh(int t) {
		team = t;
		altBox.setActionCommand("n");
		altBox.setSelectedIndex(0);
		altBox.setActionCommand("y");
		// countForm();
		ok = false;
		squadList.refresh(t, false);
		ok = true;
		int tt = t;
		if (t > 63) {
			tt = t + 9;
		}
		numList.refresh(tt);
		posList.alt = altBox.getSelectedIndex();
		posList.refresh(t);
		updateRoleBox();
		sFK.refresh(t);
		lFK.refresh(t);
		rCR.refresh(t);
		lCR.refresh(t);
		pk.refresh(t);
		cap.refresh(t);
		teamSetPan.alt = altBox.getSelectedIndex();
		teamSetPan.refresh(t);
		stratPan.refresh(t);
		pitchPanel.selected = -1;
		pitchPanel.squad = t;
		pitchPanel.repaint();
		adPanel.selected = -1;
		adPanel.squad = t;
		adPanel.repaint();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (fromPitch) {
			fromPitch = false;
			updateRoleBox();
		} else {
			if (e.getValueIsAdjusting() == false && ok) {
				int i = squadList.getSelectedIndex();
				updateRoleBox();
				if (i >= 0 && i < 11) {
					pitchPanel.selected = i;
					adPanel.selected = i;
				} else {
					pitchPanel.selected = -1;
					adPanel.selected = -1;
				}
				pitchPanel.repaint();
				adPanel.repaint();
				// posList.selectPos(squadList, i);
			}
		}
	}

	public boolean saveComponentAsPNG(Component comp, File dest) {
		boolean done = false;
		Dimension size = comp.getSize();
		byte[] red = new byte[8];
		byte[] green = new byte[8];
		byte[] blue = new byte[8];
		for (int i = 0; i < 8; i++) {
			red[i] = (byte) (pitchPanel.colour[i].getRed());
			green[i] = (byte) (pitchPanel.colour[i].getGreen());
			blue[i] = (byte) (pitchPanel.colour[i].getBlue());
		}
		IndexColorModel colMod = new IndexColorModel(8, 8, red, green, blue);
		BufferedImage image = new BufferedImage(size.width, size.height,
				BufferedImage.TYPE_BYTE_INDEXED, colMod);
		Graphics2D g2 = image.createGraphics();
		comp.paint(g2);
		try {
			ImageIO.write(image, "png", dest);
			done = true;
		} catch (Exception e) {
			// System.out.println(e);
		}
		return done;
	}

	private void updateRoleBox() {
		countForm();
		roleBox.setActionCommand("n");
		roleBox.removeAllItems();
		int si = squadList.getSelectedIndex();
		int selPos = Formations.getPos(of, team, altBox.getSelectedIndex(), si);
		roleBox.setEnabled(true);
		if (si > 0 && si < 11) {
			int count = 0;
			boolean free;
			boolean cbt = false;
			int pos;
			Role last = null;
			Role first = new Role(selPos);
			roleBox.addItem(first);
			for (int r = 1; r < 41; r++) {
				// if ((isDef(r) && isDef(pos)) || isMid(r) && isMid(pos)) ||
				// isAtt(r) && isAtt(pos))) {
				free = true;
				// } else {
				if (r == 5) {
					free = false;
				} else {

					if (r == 15) {
						for (int p = 0; free && p < 11; p++) {
							pos = Formations.getPos(of, team, altBox.getSelectedIndex(), p);
							if (pos != selPos) {
								if (pos == 8 || pos == 22) {
									free = false;
								}
							}
						}
					}

					if (r == 16) {
						for (int p = 0; free && p < 11; p++) {
							pos = Formations.getPos(of, team, altBox.getSelectedIndex(), p);
							if (pos != selPos) {
								if (pos == 9 || pos == 23) {
									free = false;
								}
							}
						}
					}

					if (selPos != 15 && (r == 8 || r == 22)) {
						for (int p = 0; free && p < 11; p++) {
							pos = Formations.getPos(of, team, altBox.getSelectedIndex(), p);
							if (pos == 15) {
								free = false;
							}
						}
					}

					if (selPos != 16 && (r == 9 || r == 23)) {
						for (int p = 0; free && p < 11; p++) {
							pos = Formations.getPos(of, team, altBox.getSelectedIndex(), p);
							if (pos == 16) {
								free = false;
							}
						}
					}

					if (isDef(selPos)) {
						if (def <= 2 && !isDef(r)) {
							free = false;
						}
						if (mid >= 6 && isMid(r)) {
							free = false;
						}
						if (att >= 5 && isAtt(r)) {
							free = false;
						}
					}

					if (isMid(selPos)) {
						if (mid <= 2 && !isMid(r)) {
							free = false;
						}
						if (def >= 5 && isDef(r)) {
							free = false;
						}
						if (att >= 5 && isAtt(r)) {
							free = false;
						}
					}

					if (isAtt(selPos)) {
						if (att <= 1 && !isAtt(r)) {
							free = false;
						}
						if (mid >= 6 && isMid(r)) {
							free = false;
						}
						if (def >= 5 && isDef(r)) {
							free = false;
						}
					}
				}
				// }

				for (int p = 0; free && p < 11; p++) {
					// System.out.println(r + ", " + p);
					pos = Formations.getPos(of, team, altBox.getSelectedIndex(), p);
					// System.out.println(a + "=" + of.data[a]);
					if (pos == r) {
						free = false;
					}
				}
				if (free) {
					Role role = new Role(r);
					if (!(first.name.equals(role.name))) {
						if (last == null) {
							last = role;
							roleBox.addItem(role);
							count++;
						} else {
							if (!(last.name.equals(role.name))) {
								if (!role.name.equals("CBT")
										|| (role.name.equals("CBT") && !cbt)) {
									last = new Role(r);
									roleBox.addItem(last);
									count++;
								}
							}
						}
						if (role.name.equals("CBT")) {
							cbt = true;
						}
					}
				}
			}

			// roleBox.setSelectedItem();
		} else {
			if (si == 0) {
				roleBox.addItem(new Role(0));
			} else {
				roleBox.setEnabled(false);
			}
		}
		roleBox.setActionCommand("y");
	}

	private class Role {
		String name;

		int index;

		public Role(int i) {
			name = "---";
			index = i;
			if (index == 0) {
				name = "GK";
			}
			if ((index > 0 && index < 4) || (index > 5 && index < 8)) {
				name = "CBT";
			}
			if (index == 4) {
				name = "CWP";
			}
			if (index == 5) {
				name = "ASW";
			}
			if (index == 8) {
				name = "LB";
			}
			if (index == 9) {
				name = "RB";
			}
			if (index > 9 && index < 15) {
				name = "DM";
			}
			if (index == 15) {
				name = "LWB";
			}
			if (index == 16) {
				name = "RWB";
			}

			if (index > 16 && index < 22) {
				name = "CM";
			}
			if (index == 22) {
				name = "LM";
			}
			if (index == 23) {
				name = "RM";
			}
			if (index > 23 && index < 29) {
				name = "AM";
			}
			if (index == 29) {
				name = "LW";
			}
			if (index == 30) {
				name = "RW";
			}
			if (index > 30 && index < 36) {
				name = "SS";
			}
			if (index > 35 && index < 41) {
				name = "CF";
			}

			if (index > 40) {
				name = String.valueOf(index);
			}
		}

		public String toString() {
			return name;
		}
	}

	private void countForm() {
		def = 0;
		mid = 0;
		att = 0;
		int pos;
		for (int i = 1; i < 11; i++) {
			pos = Formations.getPos(of, team, altBox.getSelectedIndex(), i);
			if (isDef(pos)) {
				def++;
			} else if (isMid(pos)) {
				mid++;
			} else if (isAtt(pos)) {
				att++;
			}
		}

		formBox.setActionCommand("n");
		formName[0] = String.valueOf(def) + "-" + String.valueOf(mid) + "-"
				+ String.valueOf(att) + " *";
		DefaultComboBoxModel model = new DefaultComboBoxModel(formName);
		formBox.setModel(model);
		formBox.setActionCommand("y");
		// System.out.println(def + "-" + mid + "-" + att);
	}

	private void savePNG() {
		boolean error = false;
		int returnVal = chooserPNG.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File dest = chooserPNG.getSelectedFile();
			if ((PESUtils.getExtension(dest) == null)
					|| !(PESUtils.getExtension(dest).equals(PESUtils.png))) {
				// System.out.println(dest);
				dest = new File(dest.getParent() + File.separator
						+ dest.getName() + ".png");
			}
			if (dest.exists()) {
				int n = JOptionPane.showConfirmDialog(null, dest.getName()
						+ "\nAlready exists in:\n" + dest.getParent()
						+ "\nAre you sure you want to overwrite this file?",
						"Overwrite:  " + dest.getName(),
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null);
				if (n == 0) {
					boolean deleted = dest.delete();
					if (!deleted) {
						JOptionPane.showMessageDialog(null,
								"Could not access file", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				} else {
					return;
				}
			}

			// System.out.println(dest);
			// System.out.println(slotChooser.slot);
			if (saveComponentAsPNG(pitchPanel, dest)) {
				JOptionPane.showMessageDialog(null, dest.getName()
						+ "\nSaved in:\n" + dest.getParent(),
						"File Successfully Saved",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				error = true;
			}
			if (error) {
				JOptionPane.showMessageDialog(null, "Could not access file",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private boolean isDef(int r) {
		boolean result = false;
		if (r > 0 && r < 10) {
			result = true;
		}
		return result;
	}

	private boolean isMid(int r) {
		boolean result = false;
		if (r > 9 && r < 29) {
			result = true;
		}
		return result;
	}

	private boolean isAtt(int r) {
		boolean result = false;
		if (r > 28 && r < 41) {
			result = true;
		}
		return result;
	}

	public void dragEnter(DropTargetDragEvent event) {
	}

	public void dragExit(DropTargetEvent event) {
	}

	public void dragOver(DropTargetDragEvent event) {
		int i = squadList.locationToIndex(event.getLocation());
		Player p = (Player) (squadList.getModel().getElementAt(i));
		squadList.setSelectedIndex(i);
		if (i != -1 && i != sourceIndex && p.index != 0) {
			event.acceptDrag(DnDConstants.ACTION_MOVE);
		} else {
			event.rejectDrag();
		}
	}

	public void drop(DropTargetDropEvent event) {
		Transferable transferable = event.getTransferable();
		int ti = squadList.getSelectedIndex();
		if (transferable.isDataFlavorSupported(localPlayerFlavor)) {
			event.acceptDrop(DnDConstants.ACTION_MOVE);

			byte tb = Formations.getSlot(of, team, sourceIndex);
			Formations.setSlot(of, team, sourceIndex, Formations.getSlot(of, team, ti));
			Formations.setSlot(of, team, ti, tb);
			if (sourceIndex < 11 && ti < 11) {
				for (int j = 0; j < 6; j++) {
					if (Formations.getJob(of, team, j) == sourceIndex) {
						Formations.setJob(of, team, j, of.toByte(ti));
					} else if (Formations.getJob(of, team, j) == ti) {
						Formations.setJob(of, team, j, of.toByte(sourceIndex));
					}
				}
				sFK.refresh(team);
				lFK.refresh(team);
				rCR.refresh(team);
				lCR.refresh(team);
				pk.refresh(team);
				cap.refresh(team);
			}
			ok = false;
			int tt = team;
			if (team > 63) {
				tt = team + 9;
			}
			numList.refresh(tt);
			squadList.refresh(team, false);
			teamSetPan.refresh(team);
			stratPan.refresh(team);
			pitchPanel.repaint();
			// ti = -1;
			ok = true;
			event.getDropTargetContext().dropComplete(true);
		} else {
			event.rejectDrop();
		}
	}

	public void dropActionChanged(DropTargetDragEvent event) {
	}

	public void dragGestureRecognized(DragGestureEvent event) {
		sourceIndex = squadList.getSelectedIndex();
		Player p = (Player) squadList.getSelectedValue();
		if (sourceIndex != -1 && p.index != 0) {
			posList.selectPos(squadList, sourceIndex);

			roleBox.setActionCommand("n");
			roleBox.removeAllItems();
			roleBox.setEnabled(false);
			roleBox.setActionCommand("y");
			pitchPanel.selected = -1;
			adPanel.selected = -1;
			pitchPanel.repaint();
			adPanel.repaint();
			PlayerTransferable playerTran = new PlayerTransferable(p);
			event.getDragSource().startDrag(event, null, playerTran, this);
		} else {
			// System.out.println( "nothing was selected");
		}
	}

	public void dragDropEnd(DragSourceDropEvent event) {
		squadList.clearSelection();
		posList.clearSelection();

		/*
		 * //if (event.getDropSuccess()){} int ti =
		 * squadList.getSelectedIndex(); updateRoleBox(); if (ti >= 0 && ti <
		 * 11) { pitchPanel.selected = ti; adPanel.selected = ti; } else {
		 * pitchPanel.selected = -1; adPanel.selected = -1; }
		 * pitchPanel.repaint(); adPanel.repaint(); posList.clearSelection();
		 */
	}

	public void dragEnter(DragSourceDragEvent event) {
	}

	public void dragExit(DragSourceEvent event) {
	}

	public void dragOver(DragSourceDragEvent event) {
	}

	public void dropActionChanged(DragSourceDragEvent event) {
	}

	public class PlayerTransferable implements Transferable {
		Player data;

		public PlayerTransferable(Player p) {
			data = p;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return data;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { localPlayerFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (localPlayerFlavor.equals(flavor)) {
				return true;
			}
			return false;
		}
	}

}
