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
import java.awt.Component;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TransferPanel extends JPanel implements MouseListener,
		DropTargetListener, DragSourceListener, DragGestureListener {
	
	private SelectByTeam selectorL;

	private SelectByTeam selectorR;

	private SelectByNation freeList;

	private OptionFile of;

	private NameEditor nameEditor;

	private NumEditor numEditor;

	private InfoPanel infoPanel;

	private ShirtNameEditor shirtEditor;

	private PlayerDialog playerDia;

	private FormationDialog teamDia;

	private JCheckBox autoRel = new JCheckBox("Auto Release");

	private JCheckBox autoRep = new JCheckBox("Auto Sub");

	private JCheckBox safeMode = new JCheckBox("Safe Mode");

	private JButton compare;

	private int releasedIndex = 0;

	private DragSource sourceF = null;

	private DragSource sourceL = null;

	private DragSource sourceR = null;

	private Component sourceComp = null;

	private int sourceIndex = -1;

	private DataFlavor localPlayerFlavor;

	private int compIndex = 0;

	private int lastIndex = 0;

	public TransferPanel(PlayerDialog pd, OptionFile opf, FormationDialog td) {
		super();
		of = opf;
		teamDia = td;
		playerDia = pd;
		autoRel.setToolTipText("When a player is transfered to a club squad he will be automatically released from his old squad");
		autoRel.setSelected(true);
		autoRep
				.setToolTipText("Gaps made in a team's first 11 will be automatically filled with the most appropriate sub");
		autoRep.setSelected(true);
		safeMode
				.setToolTipText("Only transfers that are possible in-game will be allowed");
		safeMode.setSelected(true);

		compare = new JButton("Compare Stats");
		compare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent rl) {
				if (compIndex == 0) {
					compIndex = lastIndex;
					if (nameEditor.source == 2) {
						int squadS = selectorL.teamBox.getSelectedIndex();
						if (squadS < 64 || (squadS > 72 && squadS < 213)) {
							selectorL.posList.selectPos(selectorL.squadList,
									selectorL.squadList.getSelectedIndex());
						}
					} else if (nameEditor.source == 3) {
						int squadS = selectorR.teamBox.getSelectedIndex();
						if (squadS < 64 || (squadS > 72 && squadS < 213)) {
							selectorR.posList.selectPos(selectorR.squadList,
									selectorR.squadList.getSelectedIndex());
						}
					}
				} else {
					compIndex = 0;
					selectorL.posList.clearSelection();
					selectorR.posList.clearSelection();
				}
				infoPanel.refresh(lastIndex, compIndex);
			}
		});

		freeList = new SelectByNation(of);
		selectorL = new SelectByTeam(of, true);
		nameEditor = new NameEditor();
		numEditor = new NumEditor();
		shirtEditor = new ShirtNameEditor();
		JPanel editPanel = new JPanel(new GridLayout(0, 1));
		JPanel optPanel = new JPanel(new GridLayout(0, 1));
		JPanel lPanel = new JPanel(new BorderLayout());
		JPanel rPanel = new JPanel(new BorderLayout());
		selectorR = new SelectByTeam(of, true);
		addListen();
		freeList.freeList.addMouseListener(this);
		selectorL.squadList.addMouseListener(this);
		selectorR.squadList.addMouseListener(this);

		String localPlayerType = DataFlavor.javaJVMLocalObjectMimeType
				+ ";class=editor.Player";
		try {
			localPlayerFlavor = new DataFlavor(localPlayerType);
		} catch (ClassNotFoundException e) {
			System.out
					.println("FormTransferHandler: unable to create data flavor");
		}
		new DropTarget(freeList.freeList, this);
		new DropTarget(selectorL.squadList, this);
		new DropTarget(selectorR.squadList, this);
		sourceF = new DragSource();
		sourceF.createDefaultDragGestureRecognizer(freeList.freeList,
				DnDConstants.ACTION_MOVE, this);
		sourceL = new DragSource();
		sourceL.createDefaultDragGestureRecognizer(selectorL.squadList,
				DnDConstants.ACTION_MOVE, this);
		sourceR = new DragSource();
		sourceR.createDefaultDragGestureRecognizer(selectorR.squadList,
				DnDConstants.ACTION_MOVE, this);

		infoPanel = new InfoPanel(selectorL, of);

		selectorL.squadList
				.setToolTipText("Double click to edit player, right click to edit formation");
		selectorR.squadList
				.setToolTipText("Double click to edit player, right click to edit formation");
		freeList.freeList.setToolTipText("Double click to edit player");

		editPanel.add(nameEditor);
		editPanel.add(shirtEditor);
		optPanel.add(autoRel);
		optPanel.add(autoRep);
		optPanel.add(safeMode);

		JPanel editOptPan = new JPanel();
		JPanel editOptInfoPan = new JPanel(new BorderLayout());
		editOptPan.add(numEditor);
		editOptPan.add(editPanel);
		editOptPan.add(optPanel);
		editOptInfoPan.add(editOptPan, BorderLayout.NORTH);
		editOptInfoPan.add(infoPanel, BorderLayout.CENTER);
		editOptInfoPan.add(compare, BorderLayout.SOUTH);

		lPanel.add(selectorL, BorderLayout.CENTER);
		rPanel.add(selectorR, BorderLayout.CENTER);
		JPanel listPan = new JPanel(new GridLayout(0, 3));
		listPan.add(freeList);
		listPan.add(lPanel);
		listPan.add(rPanel);

		add(listPan);
		add(editOptInfoPan);
	}

	private int getNumAdr(int a) {
		return Squads.num23 + ((a - Squads.slot23) / 2);
	}

	public void refresh() {
		freeList.refresh();
		selectorL.refresh();
		selectorR.refresh();
		nameEditor.setText("");
		numEditor.setText("");
		shirtEditor.setText("");
		compIndex = 0;
		lastIndex = 0;
		infoPanel.refresh(lastIndex, compIndex);
	}

	public void refreshLists() {
		freeList.freeList.refresh(freeList.nationBox.getSelectedIndex(),
				freeList.alpha);
		selectorL.squadList.refresh(selectorL.teamBox.getSelectedIndex(), true);
		selectorR.squadList.refresh(selectorR.teamBox.getSelectedIndex(), true);
		selectorL.numList.refresh(selectorL.teamBox.getSelectedIndex());
		selectorR.numList.refresh(selectorR.teamBox.getSelectedIndex());
		selectorL.posList.refresh(selectorL.teamBox.getSelectedIndex());
		selectorR.posList.refresh(selectorR.teamBox.getSelectedIndex());
		nameEditor.setText("");
		numEditor.setText("");
		shirtEditor.setText("");
		compIndex = 0;
		lastIndex = 0;
		infoPanel.refresh(lastIndex, compIndex);
	}

	private class NameEditor extends JTextField implements
			ListSelectionListener, ActionListener {

		int source = 0;

		public NameEditor() {
			super(13);
			addActionListener(this);
			setToolTipText("Enter new name and press return");
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {
				if (e.getSource() == freeList.freeList) {
					if (freeList.freeList.isSelectionEmpty()) {
						setText("");
						lastIndex = 0;
					} else {
						setText(((Player) freeList.freeList.getSelectedValue()).name);
						source = 1;
						selectAll();
						lastIndex = ((Player) freeList.freeList
								.getSelectedValue()).index;
					}
				}
				if (e.getSource() == selectorL.squadList) {
					if (selectorL.squadList.isSelectionEmpty()) {
						setText("");
						lastIndex = 0;
					} else {
						if (((Player) selectorL.squadList.getSelectedValue()).index != 0) {
							setText(((Player) selectorL.squadList
									.getSelectedValue()).name);
						} else {
							setText("");
						}
						source = 2;
						selectAll();
						lastIndex = ((Player) selectorL.squadList
								.getSelectedValue()).index;
					}
				}
				if (e.getSource() == selectorR.squadList) {
					if (selectorR.squadList.isSelectionEmpty()) {
						setText("");
						lastIndex = 0;
					} else {
						if (((Player) selectorR.squadList.getSelectedValue()).index != 0) {
							setText(((Player) selectorR.squadList
									.getSelectedValue()).name);
						} else {
							setText("");
						}
						source = 3;
						selectAll();
						lastIndex = ((Player) selectorR.squadList
								.getSelectedValue()).index;
					}
				}
				infoPanel.refresh(lastIndex, compIndex);
			}
		}

		public void actionPerformed(ActionEvent evt) {
			if (source == 1 && !freeList.freeList.isSelectionEmpty()
					&& getText().length() < 16 && getText().length() != 0) {
				int i = freeList.freeList.getSelectedIndex();
				if (!(((Player) freeList.freeList.getSelectedValue()).name
						.equals(getText()))) {
					((Player) freeList.freeList.getSelectedValue())
							.setName(getText());
					((Player) freeList.freeList.getSelectedValue()).makeShirt(getText());
					refreshLists();
				}
				if (!freeList.alpha
						&& i < freeList.freeList.getModel().getSize() - 1) {
					freeList.freeList.setSelectedIndex(i + 1);
					freeList.freeList.ensureIndexIsVisible(i + 1);
				}
			}
			if (source == 2 && !selectorL.squadList.isSelectionEmpty()
					&& getText().length() < 16 && getText().length() != 0) {
				int i = selectorL.squadList.getSelectedIndex();
				if (!(((Player) selectorL.squadList.getSelectedValue()).name
						.equals(getText()))) {
					((Player) selectorL.squadList.getSelectedValue())
							.setName(getText());
					((Player) selectorL.squadList.getSelectedValue()).makeShirt(getText());
					refreshLists();
				}
				if (i < selectorL.squadList.getModel().getSize() - 1) {
					selectorL.squadList.setSelectedIndex(i + 1);
				}
			}
			if (source == 3 && !selectorR.squadList.isSelectionEmpty()
					&& getText().length() < 16 && getText().length() != 0) {
				int i = selectorR.squadList.getSelectedIndex();
				if (!(((Player) selectorR.squadList.getSelectedValue()).name
						.equals(getText()))) {
					((Player) selectorR.squadList.getSelectedValue())
							.setName(getText());
					((Player) selectorR.squadList.getSelectedValue()).makeShirt(getText());
					refreshLists();
				}
				if (i < selectorR.squadList.getModel().getSize() - 1) {
					selectorR.squadList.setSelectedIndex(i + 1);
				}
			}
		}
	}

	private class NumEditor extends JTextField implements
			ListSelectionListener, ActionListener {

		int source = 0;
		
		public NumEditor() {
			super(2);
			addActionListener(this);
			setToolTipText("Enter new squad number and press return");
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() == selectorL.numList) {
				if (selectorL.numList.isSelectionEmpty()) {
					setText("");
				} else {
					source = 2;
					setText(String.valueOf(getShirt(source, selectorL.numList
							.getSelectedIndex())));
					selectorR.numList.clearSelection();
					selectAll();
				}
			}
			if (e.getSource() == selectorR.numList) {
				if (selectorR.numList.isSelectionEmpty()) {
					setText("");
				} else {
					source = 3;
					setText(String.valueOf(getShirt(source, selectorR.numList
							.getSelectedIndex())));
					selectorL.numList.clearSelection();
					selectAll();
				}
			}
		}

		public void actionPerformed(ActionEvent evt) {
			if (source == 2 && !selectorL.numList.isSelectionEmpty()) {
				int i = selectorL.numList.getSelectedIndex();
				try {
					int v = new Integer(getText()).intValue();
					if (v != 0 && v <= 99) {
						setShirt(source, i, v);
					}
					selectorR.numList.refresh(selectorR.teamBox
							.getSelectedIndex());
					selectorL.numList.refresh(selectorL.teamBox
							.getSelectedIndex());
					if (i < selectorL.squadList.getModel().getSize() - 1) {
						selectorL.numList.setSelectedIndex(i + 1);
					}
				} catch (NumberFormatException nfe) {
				}
			}
			if (source == 3 && !selectorR.numList.isSelectionEmpty()) {
				int i = selectorR.numList.getSelectedIndex();
				try {
					int v = new Integer(getText()).intValue();
					if (v != 0 && v <= 99) {
						setShirt(source, i, v);
					}
					selectorR.numList.refresh(selectorR.teamBox
							.getSelectedIndex());
					selectorL.numList.refresh(selectorL.teamBox
							.getSelectedIndex());
					if (i < selectorR.squadList.getModel().getSize() - 1) {
						selectorR.numList.setSelectedIndex(i + 1);
					}
				} catch (NumberFormatException nfe) {
				}
			}
		}
	}

	public int getShirt(int s, int i) {
		int a;
		if (s == 2) {
			a = ((Player) (selectorL.squadList.getModel().getElementAt(i))).adr;
		} else {
			a = ((Player) (selectorR.squadList.getModel().getElementAt(i))).adr;
		}
		a = getNumAdr(a);
		int shirt = of.toInt(of.data[a]) + 1;
		if (shirt == 256) {
			shirt = 0;
		}
		return shirt;
	}

	public void setShirt(int s, int i, int newShirt) {
		int a;
		if (s == 2) {
			a = ((Player) (selectorL.squadList.getModel().getElementAt(i))).adr;
		} else {
			a = ((Player) (selectorR.squadList.getModel().getElementAt(i))).adr;
		}
		a = getNumAdr(a);
		int shirt = of.toInt(of.data[a]) + 1;
		if (shirt != 256) {
			of.data[a] = of.toByte(newShirt - 1);
		}
	}

	private class ShirtNameEditor extends JTextField implements
			ListSelectionListener, ActionListener {

		int source = 0;

		public ShirtNameEditor() {
			super(13);
			addActionListener(this);
			setToolTipText("Enter new shirt name and press return");
		}

		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() == false) {
				if (e.getSource() == freeList.freeList) {
					if (freeList.freeList.isSelectionEmpty()) {
						setText("");
					} else {
						setText(((Player) freeList.freeList.getSelectedValue()).getShirtName());
						source = 1;
						selectAll();
					}
				}
				if (e.getSource() == selectorL.squadList) {
					if (selectorL.squadList.isSelectionEmpty()) {
						setText("");
					} else {
						setText(((Player) selectorL.squadList.getSelectedValue()).getShirtName());
						source = 2;
						selectAll();
					}
				}
				if (e.getSource() == selectorR.squadList) {
					if (selectorR.squadList.isSelectionEmpty()) {
						setText("");
					} else {
						setText(((Player) selectorR.squadList.getSelectedValue()).getShirtName());
						source = 3;
						selectAll();
					}
				}
			}
		}

		public void actionPerformed(ActionEvent evt) {
			if (source == 1 && !freeList.freeList.isSelectionEmpty()
					&& getText().length() < 16) {
				((Player) freeList.freeList.getSelectedValue()).setShirtName(getText());
				refreshLists();
			}
			if (source == 2 && !selectorL.squadList.isSelectionEmpty()
					&& getText().length() < 16) {
				int i = selectorL.squadList.getSelectedIndex();
				((Player) selectorL.squadList.getSelectedValue()).setShirtName(getText());
				refreshLists();
				if (i < selectorL.squadList.getModel().getSize() - 1) {
					selectorL.squadList.setSelectedIndex(i + 1);
				}
			}
			if (source == 3 && !selectorR.squadList.isSelectionEmpty()
					&& getText().length() < 16) {
				int i = selectorR.squadList.getSelectedIndex();
				((Player) selectorR.squadList.getSelectedValue()).setShirtName(getText());
				refreshLists();
				if (i < selectorR.squadList.getModel().getSize() - 1) {
					selectorR.squadList.setSelectedIndex(i + 1);
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()) {
			if (e.getSource() != freeList.freeList) {
				e.consume();
				SquadList list = (SquadList) (e.getSource());
				int t = list.team;
				if (t >= 0 && t < 64) {
					teamDia.show(t, (String) selectorL.teamBox.getItemAt(t));
					Squads.fixForm(of, t, false);
					refreshLists();
				}
				if (t >= 73 && t < 213) {
					teamDia.show(t - 9, (String) selectorL.teamBox
							.getItemAt(t));
					Squads.fixForm(of, t, false);
					refreshLists();
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {
		int clicks = e.getClickCount();
		if (e.getButton() == MouseEvent.BUTTON1 && clicks == 2) {
			JList list = (JList) (e.getSource());
			Player p = ((Player) list.getSelectedValue());
			int pi = p.index;
			if (pi != 0) {
				playerDia.show(p);
				refreshLists();
			}
		} else if (clicks == 1 && e.getButton() == MouseEvent.BUTTON3) {
			if (e.getSource() != freeList.freeList) {
				SquadList list = (SquadList) (e.getSource());
				int t = list.team;
				if (t >= 0 && t < 64) {
					teamDia.show(t, (String) selectorL.teamBox.getItemAt(t));
					Squads.fixForm(of, t, false);
					refreshLists();
				}
				if (t >= 73 && t < 213) {
					teamDia.show(t - 9, (String) selectorL.teamBox
							.getItemAt(t));
					Squads.fixForm(of, t, false);
					refreshLists();
				}
			}
		}
	}

	private int clubRelease(int p, boolean rel) {
		int a = Squads.slot32 - 2;
		int index;
		int result = -1;
		int sqi;
		int sp;
		do {
			a = a + 2;
			index = (of.toInt(of.data[a + 1]) << 8) | of.toInt(of.data[a]);
			if (index == p) {
				sqi = ((a - Squads.slot32) / 64) + 73;
				sp = (a - Squads.slot32) % 64;
				if (result == -1) {
					if (!rel || sp < 22) {
						result = sqi;
						if (rel) {
							releasedIndex = sp / 2;
						}

					}
				}
				if (rel) {
					of.data[a] = 0;
					of.data[a + 1] = 0;
					of.data[getNumAdr(a)] = -1;
					if (sp >= 22) {
						Squads.tidy(of, sqi);
					} else {
						if (autoRep.isSelected()) {
							int t = sqi;
							if (t > 72) {
								t = t - 9;
							}
							Squads.tidy11(of, sqi, sp / 2, Formations.getPos(of, t, 0, sp / 2));
						}
					}
				}
			}
		} while (a < Squads.slot32 + (Clubs.total * 64) - 2);// && index != p);
		return result;
	}

	private byte getNextNum(int s) {
		int size;
		int firstAdr;
		byte num = -1;
		int a;
		byte n;
		boolean spare;
		if (s < 73) {
			size = 23;
			firstAdr = Squads.num23 + (s * size);
		} else {
			size = 32;
			firstAdr = Squads.num32 + ((s - 73) * size);
		}
		for (byte i = 0; num == -1 && i < 99; i++) {
			spare = true;
			for (int p = 0; spare && p < size; p++) {
				a = firstAdr + p;
				n = of.data[a];
				if (n == i) {
					spare = false;
				}
			}
			if (spare) {
				num = i;
			}
		}
		if (num == -1) {
			num = 0;
		}
		return num;
	}

	private int countPlayers(int squad) {
		int size;
		int firstAdr;
		int i;
		int count = 0;
		int a;
		if (squad < 73) {
			size = 23;
			firstAdr = Squads.slot23 + (squad * size * 2);
		} else {
			size = 32;
			firstAdr = Squads.slot32 + ((squad - 73) * size * 2);
		}
		for (int p = 0; p < size; p++) {
			a = firstAdr + (p * 2);
			i = (of.toInt(of.data[a + 1]) << 8) | of.toInt(of.data[a]);
			if (i != 0) {
				count++;
			}
		}
		return count;
	}

	private boolean inSquad(int squad, int pi) {
		boolean in = false;
		if (pi != 0) {
			int size;
			int firstAdr;
			int i;
			int a;
			if (squad < 73) {
				size = 23;
				firstAdr = Squads.slot23 + (squad * size * 2);
			} else {
				size = 32;
				firstAdr = Squads.slot32 + ((squad - 73) * size * 2);
			}
			for (int p = 0; !in && p < size; p++) {
				a = firstAdr + (p * 2);
				i = (of.toInt(of.data[a + 1]) << 8) | of.toInt(of.data[a]);
				if (i == pi) {
					in = true;
				}
			}
		}
		return in;
	}

	public void dragEnter(DropTargetDragEvent event) {
	}

	public void dragExit(DropTargetEvent event) {
	}

	public void dragOver(DropTargetDragEvent event) {
		JList targetList = (JList) (event.getDropTargetContext().getComponent());
		int i = targetList.locationToIndex(event.getLocation());
		Player p;
		if (i != -1) {
			p = (Player) (targetList.getModel().getElementAt(i));
		} else {
			p = new Player(of, 0, 0);
		}
		boolean chk = checkSafeDrag(safeMode.isSelected(), targetList, p);
		targetList.setSelectedIndex(i);
		if (chk) {
			event.acceptDrag(DnDConstants.ACTION_MOVE);
		} else {
			event.rejectDrag();
		}
	}

	public void drop(DropTargetDropEvent event) {
		Transferable transferable = event.getTransferable();
		if (transferable.isDataFlavorSupported(localPlayerFlavor)) {
			JList sourceList = (JList) sourceComp;
			JList targetList = (JList) (event.getDropTargetContext()
					.getComponent());
			Player sourcePlayer = (Player) (sourceList.getModel()
					.getElementAt(sourceIndex));
			int indexS = sourcePlayer.index;
			Player targetPlayer;
			int indexT;
			if (targetList.getSelectedIndex() != -1) {
				targetPlayer = (Player) (targetList.getSelectedValue());
				indexT = targetPlayer.index;
			} else {
				targetPlayer = new Player(of, 0, 0);
				indexT = 0;
			}

			if (sourceList != freeList.freeList
					&& targetList != freeList.freeList) {
				int squadS = ((SelectByTeam) (sourceList.getParent())).teamBox
						.getSelectedIndex();
				int squadT = ((SelectByTeam) (targetList.getParent())).teamBox
						.getSelectedIndex();
				if (sourceList == targetList) {
					if (squadS < 64 || (squadS > 72 && squadS < 213)) {
						if (indexS != indexT) {
							event.acceptDrop(DnDConstants.ACTION_MOVE);
							transferS(sourcePlayer, targetPlayer, squadS,
									squadT, sourceList, targetList);
						}
					}
				} else if (sourceList == selectorL.squadList
						&& targetList == selectorR.squadList) {
					event.acceptDrop(DnDConstants.ACTION_MOVE);
					transferLR(sourcePlayer);
				} else if (sourceList == selectorR.squadList
						&& targetList == selectorL.squadList) {
					event.acceptDrop(DnDConstants.ACTION_MOVE);
					transferRL(sourcePlayer);
				}
			} else if (sourceList == freeList.freeList
					&& targetList == selectorL.squadList) {
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				transferFL(indexS);
			} else if (sourceList == freeList.freeList
					&& targetList == selectorR.squadList) {
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				transferFR(indexS);
			} else if (sourceList == selectorL.squadList
					&& targetList == freeList.freeList) {
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				tranRelL(sourcePlayer, sourceIndex);
			} else if (sourceList == selectorR.squadList
					&& targetList == freeList.freeList) {
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				tranRelR(sourcePlayer, sourceIndex);
			} else {
				event.rejectDrop();
			}

			event.getDropTargetContext().dropComplete(true);
		} else {
			event.rejectDrop();
		}
	}

	public void dropActionChanged(DropTargetDragEvent event) {
	}

	public void dragGestureRecognized(DragGestureEvent event) {
		sourceComp = event.getComponent();
		if (sourceComp instanceof JList) {
			JList list = (JList) sourceComp;
			sourceIndex = list.getSelectedIndex();
			Player p = (Player) list.getSelectedValue();
			if (sourceIndex != -1 && p.index != 0) {
				removeListen();
				lastIndex = 0;
				compIndex = 0;
				infoPanel.refresh(lastIndex, compIndex);
				nameEditor.setText("");
				shirtEditor.setText("");
				nameEditor.source = 0;
				shirtEditor.source = 0;
				PlayerTransferable playerTran = new PlayerTransferable(p);
				if (list != freeList.freeList) {
					int squadS = ((SelectByTeam) (list.getParent())).teamBox
							.getSelectedIndex();
					if (squadS < 64 || (squadS > 72 && squadS < 213)) {
						if (list == selectorL.squadList) {
							selectorL.posList.selectPos(selectorL.squadList,
									selectorL.squadList.getSelectedIndex());
						} else if (list == selectorR.squadList) {
							selectorR.posList.selectPos(selectorR.squadList,
									selectorR.squadList.getSelectedIndex());
						}
					}
				}
				event.getDragSource().startDrag(event, null, playerTran, this);

			}
		}
	}

	public void dragDropEnd(DragSourceDropEvent event) {
		if (!event.getDropSuccess()) {
			refreshLists();
		}
		addListen();
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

	private boolean checkSafeDrag(boolean safe, JList targetList,
			Player targetPlayer) {
		boolean tranFL = true;
		boolean tranFR = true;
		boolean tranLR = true;
		boolean tranRL = true;
		boolean relL = true;
		boolean relR = true;
		boolean fEmpty = true;
		boolean lEmpty = true;
		boolean rEmpty = true;
		int squadL = -1;
		int squadR = -1;

		JList sourceList = (JList) sourceComp;
		int indexS = ((Player) (sourceList.getModel().getElementAt(sourceIndex))).index;
		int indexT = targetPlayer.index;
		int squadS = -1;

		int indexF = 0;
		if (sourceList == freeList.freeList) {
			indexF = indexS;
			fEmpty = false;
		} else if (targetList == freeList.freeList) {
			indexF = indexT;
			fEmpty = false;
		}
		int indexL = 0;
		if (sourceList == selectorL.squadList) {
			indexL = indexS;
			lEmpty = false;
			squadS = ((SelectByTeam) (sourceList.getParent())).teamBox
					.getSelectedIndex();
		} else if (targetList == selectorL.squadList) {
			indexL = indexT;
			lEmpty = false;
		}
		int indexR = 0;
		if (sourceList == selectorR.squadList) {
			indexR = indexS;
			rEmpty = false;
			squadS = ((SelectByTeam) (sourceList.getParent())).teamBox
					.getSelectedIndex();
		} else if (targetList == selectorR.squadList) {
			indexR = indexT;
			rEmpty = false;
		}

		squadL = selectorL.teamBox.getSelectedIndex();
		squadR = selectorR.teamBox.getSelectedIndex();
		if (safe) {
			int minSizeL = 16;
			int minSizeR = 16;
			if (squadL < 73) {
				minSizeL = 23;
			}
			if (squadR < 73) {
				minSizeR = 23;
			}

			if (indexF >= Player.firstYoung && indexF < Player.firstEdit) {
				tranFL = false;
				tranFR = false;
			}

			if (indexF >= Player.firstML && indexF < Player.firstShop) {
				tranFL = false;
				tranFR = false;
			}

			if (indexF >= Player.firstEdit && indexF < 32920 && squadL > 56) {
				tranFL = false;
			}

			if (indexF >= Player.firstEdit && indexF < 32920 && squadR > 56) {
				tranFR = false;
			}

			if (squadL < 64 && squadR > 63) {
			}
			if (squadR < 64 && squadL > 63) {
			}

			if (squadL > 72 && squadL < 213) {
				int s = clubRelease(indexF, false);
				if (autoRel.isSelected()) {
					if (s != -1) {
						int c = countPlayers(s);
						if (c <= 16) {
							tranFL = false;
						}
					}
				} else {
					if (s != -1) {
						tranFL = false;
					}
				}
			}

			if (squadR > 72 && squadR < 213) {
				int s = clubRelease(indexF, false);
				if (autoRel.isSelected()) {
					if (s != -1) {
						int c = countPlayers(s);
						if (c <= 16) {
							tranFR = false;
						}
					}
				} else {
					if (s != -1) {
						tranFR = false;
					}
				}
			}

			if ((squadL > 56 && squadL < 73) || squadL > 212) {
				tranFL = false;
				if (squadL == 64 || squadL == 213) {
					tranLR = false;
				} else {
					if (squadL > 64 && squadL < 72 && squadR > 63) {
						tranLR = false;
					}
				}
				tranRL = false;
				relL = false;
			} else {
				int countL = countPlayers(squadL);
				if (countL <= minSizeL) {
					relL = false;
					if (indexR == 0) {
					}
					if (autoRel.isSelected() && squadL > 72) {
						tranLR = false;
					}
				}
				if (inSquad(squadL, indexR)) {
					tranRL = false;
					if (squadL != squadR) {
					}
				}
				if (inSquad(squadL, indexF)) {
					tranFL = false;
				}

				if (!autoRel.isSelected() && squadL > 72 && squadL < 213) {
					int s = clubRelease(indexR, false);
					if (s != -1) {
						tranRL = false;
					}
				}

			}

			if ((squadR > 56 && squadR < 73) || squadR > 212) {
				tranLR = false;
				tranFR = false;
				if (squadR == 64 || squadR == 213) {
					tranRL = false;
				} else {
					if (squadR > 64 && squadR < 72 && squadL > 63) {
						tranRL = false;
					}
				}
				relR = false;
			} else {
				int countR = countPlayers(squadR);
				if (countR <= minSizeR) {
					relR = false;
					if (indexL == 0) {
					}
					if (autoRel.isSelected() && squadR > 72) {
						tranRL = false;
					}
				}
				if (inSquad(squadR, indexL)) {
					tranLR = false;
					if (squadL != squadR) {
					}
				}

				if (inSquad(squadR, indexF)) {
					tranFR = false;
				}

				if (!autoRel.isSelected() && squadR > 72 && squadR < 213) {
					int s = clubRelease(indexL, false);
					if (s != -1) {
						tranLR = false;
					}
				}
			}

			if (squadR == squadL) {
				tranLR = false;
				tranRL = false;
			}

			if (squadL < 64) {
				int squadNat = squadL;
				switch (squadNat) {
				case 57:
					squadNat = 6;
					break;
				case 58:
					squadNat = 8;
					break;
				case 59:
					squadNat = 9;
					break;
				case 60:
					squadNat = 13;
					break;
				case 61:
					squadNat = 15;
					break;
				case 62:
					squadNat = 44;
					break;
				case 63:
					squadNat = 45;
					break;
				}

				int nat;
				if (!fEmpty) {
					nat = Stats.getValue(of, indexF, Stats.nationality);
					if (nat != (Stats.nation.length - 1) && nat != squadNat) {
						tranFL = false;
					}
				}
				if (!rEmpty) {
					nat = Stats.getValue(of, indexR, Stats.nationality);
					if (nat != (Stats.nation.length - 1) && nat != squadNat) {
						tranRL = false;
					}
				}
			}

			if (squadR < 64) {
				int squadNat = squadR;
				switch (squadNat) {
				case 57:
					squadNat = 6;
					break;
				case 58:
					squadNat = 8;
					break;
				case 59:
					squadNat = 9;
					break;
				case 60:
					squadNat = 13;
					break;
				case 61:
					squadNat = 15;
					break;
				case 62:
					squadNat = 44;
					break;
				case 63:
					squadNat = 45;
					break;
				}

				int nat;
				if (!fEmpty) {
					nat = Stats.getValue(of, indexF, Stats.nationality);
					if (nat != (Stats.nation.length - 1) && nat != squadNat) {
						tranFR = false;
					}
				}
				if (!lEmpty) {
					nat = Stats.getValue(of, indexL, Stats.nationality);
					if (nat != (Stats.nation.length - 1) && nat != squadNat) {
						tranLR = false;
					}
				}
			}

		}
		boolean result = false;

		if (sourceList != freeList.freeList && targetList != freeList.freeList) {
			if (sourceList == targetList) {
				if (squadS < 64 || (squadS > 72 && squadS < 213)) {
					if (indexS != indexT) {
						result = true;
					}
				}
			} else if (sourceList == selectorL.squadList
					&& targetList == selectorR.squadList && tranLR
					&& indexS != 0) {
				result = true;
			} else if (sourceList == selectorR.squadList
					&& targetList == selectorL.squadList && tranRL
					&& indexS != 0) {
				result = true;
			}
		} else if (sourceList == freeList.freeList
				&& targetList == selectorL.squadList && tranFL) {
			result = true;
		} else if (sourceList == freeList.freeList
				&& targetList == selectorR.squadList && tranFR) {
			result = true;
		} else if (sourceList == selectorL.squadList
				&& targetList == freeList.freeList && relL) {
			result = true;
		} else if (sourceList == selectorR.squadList
				&& targetList == freeList.freeList && relR) {
			result = true;
		}
		return result;
	}

	private void transferFL(int index) {
		int adr = ((Player) (selectorL.squadList.getSelectedValue())).adr;
		int ti = selectorL.teamBox.getSelectedIndex();
		int n = -1;
		if (ti >= 73 && ti < 213 && autoRel.isSelected()) {
			n = clubRelease(index, true);
		}
		of.data[adr] = of.toByte(index & 0x000000FF);
		of.data[adr + 1] = of.toByte((index & 0x0000FF00) >>> 8);
		if (of.data[getNumAdr(adr)] == -1) {
			of.data[getNumAdr(adr)] = getNextNum(ti);
		}
		if (selectorL.squadList.getSelectedIndex() > 10) {
			Squads.tidy(of, ti);
		}
		refreshLists();
		if (n != -1) {
			selectorR.teamBox.setSelectedIndex(n);
			selectorR.posList.clearSelection();
			selectorR.posList.setSelectedIndex(releasedIndex);
		}
	}

	private void transferFR(int index) {
		int adr = ((Player) (selectorR.squadList.getSelectedValue())).adr;
		int ti = selectorR.teamBox.getSelectedIndex();
		int n = -1;
		if (ti >= 73 && ti < 213 && autoRel.isSelected()) {
			n = clubRelease(index, true);
		}
		of.data[adr] = of.toByte(index & 0x000000FF);
		of.data[adr + 1] = of.toByte((index & 0x0000FF00) >>> 8);
		if (of.data[getNumAdr(adr)] == -1) {
			of.data[getNumAdr(adr)] = getNextNum(ti);
		}
		if (selectorR.squadList.getSelectedIndex() > 10) {
			Squads.tidy(of, ti);
		}
		refreshLists();
		if (n != -1) {
			selectorL.teamBox.setSelectedIndex(n);
			selectorL.posList.clearSelection();
			selectorL.posList.setSelectedIndex(releasedIndex);
		}
	}

	private void transferLR(Player player) {
		int adrR = ((Player) (selectorR.squadList.getSelectedValue())).adr;
		int index = player.index;
		if (index != 0) {
			int tiR = selectorR.teamBox.getSelectedIndex();
			int tiL = selectorL.teamBox.getSelectedIndex();
			int n = -1;
			if (tiR >= 73 && tiR < 213 && autoRel.isSelected()) {
				n = clubRelease(index, true);
			}

			of.data[adrR] = of.toByte(index & 0x000000FF);
			of.data[adrR + 1] = of.toByte((index & 0x0000FF00) >>> 8);
			if (of.data[getNumAdr(adrR)] == -1) {
				of.data[getNumAdr(adrR)] = getNextNum(tiR);
			}
			if (selectorR.squadList.getSelectedIndex() > 10) {
				Squads.tidy(of, selectorR.teamBox.getSelectedIndex());
			}

			refreshLists();
			if (n != -1 && (tiL < 73 || tiL > 212)) {
				selectorL.teamBox.setSelectedIndex(n);
				selectorL.posList.clearSelection();
				selectorL.posList.setSelectedIndex(releasedIndex);
			}
		}
	}

	private void transferRL(Player player) {
		int adrL = ((Player) (selectorL.squadList.getSelectedValue())).adr;
		int index = player.index;
		if (index != 0) {
			int tiL = selectorL.teamBox.getSelectedIndex();
			int tiR = selectorR.teamBox.getSelectedIndex();
			int n = -1;
			if (tiL >= 73 && tiL < 213 && autoRel.isSelected()) {
				n = clubRelease(index, true);
			}

			of.data[adrL] = of.toByte(index & 0x000000FF);
			of.data[adrL + 1] = of.toByte((index & 0x0000FF00) >>> 8);
			if (of.data[getNumAdr(adrL)] == -1) {
				of.data[getNumAdr(adrL)] = getNextNum(tiL);
			}
			if (selectorL.squadList.getSelectedIndex() > 10) {
				Squads.tidy(of, selectorL.teamBox.getSelectedIndex());
			}

			refreshLists();
			if (n != -1 && (tiR < 73 || tiR > 212)) {
				selectorR.teamBox.setSelectedIndex(n);
				selectorR.posList.clearSelection();
				selectorR.posList.setSelectedIndex(releasedIndex);
			}
		}
	}

	private void transferS(Player playerS, Player playerT, int tiS, int tiT,
			JList sourceList, JList targetList) {
		int adrS = playerS.adr;
		int indexS = playerS.index;
		int adrT = playerT.adr;
		int indexT = playerT.index;

		of.data[adrS] = of.toByte(indexT & 0x000000FF);
		of.data[adrS + 1] = of.toByte((indexT & 0x0000FF00) >>> 8);
		of.data[adrT] = of.toByte(indexS & 0x000000FF);
		of.data[adrT + 1] = of.toByte((indexS & 0x0000FF00) >>> 8);

		if (tiS == tiT) {
			byte t = of.data[getNumAdr(adrT)];
			of.data[getNumAdr(adrT)] = of.data[getNumAdr(adrS)];
			of.data[getNumAdr(adrS)] = t;
		}

		if (indexS == 0 || indexT == 0) {
			if (sourceIndex > 10) {
				Squads.tidy(of, tiS);
			} else {
				if (autoRep.isSelected()) {
					Squads
							.tidy11(
									of, tiS,
									sourceIndex,
									((SelectByTeam) (sourceList.getParent())).posList.posNum[sourceIndex]);
				}
			}
			if (targetList.getSelectedIndex() > 10) {
				Squads.tidy(of, tiT);
			} else {
				if (autoRep.isSelected() && sourceList != targetList) {
					Squads
							.tidy11(
									of, tiT,
									targetList.getSelectedIndex(),
									((SelectByTeam) (targetList.getParent())).posList.posNum[targetList
											.getSelectedIndex()]);
				}
			}
		}

		refreshLists();
	}

	private void tranRelL(Player player, int si) {
		int adr = player.adr;
		of.data[adr] = 0;
		of.data[adr + 1] = 0;
		of.data[getNumAdr(adr)] = -1;
		if (si > 10) {
			Squads.tidy(of, selectorL.teamBox.getSelectedIndex());
		} else {
			if (autoRep.isSelected()) {
				Squads.tidy11(of, selectorL.teamBox.getSelectedIndex(), si,
						selectorL.posList.posNum[si]);
			}
		}
		refreshLists();
	}

	private void tranRelR(Player player, int si) {
		int adr = player.adr;
		of.data[adr] = 0;
		of.data[adr + 1] = 0;
		of.data[getNumAdr(adr)] = -1;
		if (si > 10) {
			Squads.tidy(of, selectorR.teamBox.getSelectedIndex());
		} else {
			if (autoRep.isSelected()) {
				Squads.tidy11(of, selectorR.teamBox.getSelectedIndex(), si,
						selectorR.posList.posNum[si]);
			}
		}
		refreshLists();
	}

	private void addListen() {
		selectorL.squadList.addListSelectionListener(nameEditor);
		selectorR.squadList.addListSelectionListener(nameEditor);
		freeList.freeList.addListSelectionListener(nameEditor);
		selectorL.squadList.addListSelectionListener(shirtEditor);
		selectorR.squadList.addListSelectionListener(shirtEditor);
		freeList.freeList.addListSelectionListener(shirtEditor);
		selectorL.numList.addListSelectionListener(numEditor);
		selectorR.numList.addListSelectionListener(numEditor);
	}

	private void removeListen() {
		selectorL.squadList.removeListSelectionListener(nameEditor);
		selectorR.squadList.removeListSelectionListener(nameEditor);
		freeList.freeList.removeListSelectionListener(nameEditor);
		selectorL.squadList.removeListSelectionListener(shirtEditor);
		selectorR.squadList.removeListSelectionListener(shirtEditor);
		freeList.freeList.removeListSelectionListener(shirtEditor);
		selectorL.numList.removeListSelectionListener(numEditor);
		selectorR.numList.removeListSelectionListener(numEditor);
	}

}
