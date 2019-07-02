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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class AttDefPanel extends JPanel implements MouseListener {
	OptionFile of;

	int squad = 0;

	int selected = -1;

	PitchPanel pitch;

	Rectangle2D[] attSqu = new Rectangle2D[8];

	JComboBox altBox;

	public AttDefPanel(OptionFile opf, JComboBox ab) {
		super();
		of = opf;
		altBox = ab;
		setOpaque(true);
		setPreferredSize(new Dimension(98, 98));
		setBackground(Color.black);
		addMouseListener(this);
		attSqu[0] = new Rectangle2D.Double(0, 42, 14, 14);
		attSqu[1] = new Rectangle2D.Double(0, 0, 14, 14);
		attSqu[2] = new Rectangle2D.Double(42, 0, 14, 14);
		attSqu[3] = new Rectangle2D.Double(84, 0, 14, 14);
		attSqu[4] = new Rectangle2D.Double(84, 42, 14, 14);
		attSqu[5] = new Rectangle2D.Double(84, 84, 14, 14);
		attSqu[6] = new Rectangle2D.Double(42, 84, 14, 14);
		attSqu[7] = new Rectangle2D.Double(0, 84, 14, 14);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (selected == -1) {
			g2.setPaint(Color.gray);
			g2.fill(new Rectangle2D.Double(0, 0, 98, 98));
		} else {
			g2.setPaint(Color.black);
			g2.fill(new Rectangle2D.Double(0, 0, 98, 98));

			int pos = Formations.getPos(of, squad, altBox.getSelectedIndex(), selected);
			if (pos == 0) {
				g2.setPaint(Color.yellow);
			} else if (pos > 0 && pos < 10) {
				g2.setPaint(Color.cyan);
			} else if (pos > 9 && pos < 29) {
				g2.setPaint(Color.green);
			} else if (pos > 28 && pos < 41) {
				g2.setPaint(Color.red);
			}
			g2.fill(new Ellipse2D.Double(42, 42, 14, 14));

			for (int i = 0; i < 8; i++) {
				if (Formations.getAtk(of, squad, altBox.getSelectedIndex(), selected, i)) {
					if (pos == 0) {
						g2.setPaint(Color.yellow);
					} else if (pos > 0 && pos < 10) {
						g2.setPaint(Color.cyan);
					} else if (pos > 9 && pos < 29) {
						g2.setPaint(Color.green);
					} else if (pos > 28 && pos < 41) {
						g2.setPaint(Color.red);
					}
					g2.fill(attSqu[i]);
					/*
					 * switch (i) { case 0: g2.fill(new Rectangle2D.Double(0,
					 * 42, 14, 14)); break; case 1: g2.fill(new
					 * Rectangle2D.Double(0, 0, 14, 14)); break; case 2:
					 * g2.fill(new Rectangle2D.Double(42, 0, 14, 14)); break;
					 * case 3: g2.fill(new Rectangle2D.Double(84, 0, 14, 14));
					 * break; case 4: g2.fill(new Rectangle2D.Double(84, 42, 14,
					 * 14)); break; case 5: g2.fill(new Rectangle2D.Double(84,
					 * 84, 14, 14)); break; case 6: g2.fill(new
					 * Rectangle2D.Double(42, 84, 14, 14)); break; case 7:
					 * g2.fill(new Rectangle2D.Double(0, 84, 14, 14)); break; }
					 */
				} else {
					g2.setPaint(Color.gray);
					if (selected != 0) {
						g2.draw(attSqu[i]);
					} else if (i == 0 || i == 4) {
						g2.draw(attSqu[i]);
					}
					/*
					 * switch (i) { case 0: g2.draw(new Rectangle2D.Double(0,
					 * 42, 14, 14)); break; case 1: g2.draw(new
					 * Rectangle2D.Double(0, 0, 14, 14)); break; case 2:
					 * g2.draw(new Rectangle2D.Double(42, 0, 14, 14)); break;
					 * case 3: g2.draw(new Rectangle2D.Double(84, 0, 14, 14));
					 * break; case 4: g2.draw(new Rectangle2D.Double(84, 42, 14,
					 * 14)); break; case 5: g2.draw(new Rectangle2D.Double(84,
					 * 84, 14, 14)); break; case 6: g2.draw(new
					 * Rectangle2D.Double(42, 84, 14, 14)); break; case 7:
					 * g2.draw(new Rectangle2D.Double(0, 84, 14, 14)); break; }
					 */
				}
			}

			if (Formations.getDef(of, squad, altBox.getSelectedIndex(), selected) == 1) {
				g2.setPaint(Color.gray);
				g2.draw(new Ellipse2D.Double(21, 21, 14, 14));
				g2.setPaint(Color.blue);
				g2.fill(new Ellipse2D.Double(21, 63, 14, 14));
			} else if (Formations.getDef(of, squad, altBox.getSelectedIndex(), selected) == 0) {
				g2.setPaint(Color.blue);
				g2.fill(new Ellipse2D.Double(21, 21, 14, 14));
				g2.fill(new Ellipse2D.Double(21, 63, 14, 14));
			} else {
				g2.setPaint(Color.gray);
				g2.draw(new Ellipse2D.Double(21, 21, 14, 14));
				g2.draw(new Ellipse2D.Double(21, 63, 14, 14));
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		// Checks whether or not the cursor is inside of the rectangle
		// while the user is pressing the mouse.
		int count = 0;
		boolean boo = true;
		byte def = Formations.getDef(of, squad, altBox.getSelectedIndex(), selected);
		if (new Ellipse2D.Double(42, 42, 14, 14).contains(e.getX(), e.getY())) {
			Formations.setAtk(of, squad, altBox.getSelectedIndex(), selected, -1);
			Formations.setDef(of, squad, altBox.getSelectedIndex(), selected, 2);
		} else if (new Ellipse2D.Double(21, 21, 14, 14).contains(e.getX(), e
				.getY())) {
			if (def == 0) {
				Formations.setDef(of, squad, altBox.getSelectedIndex(), selected, 1);
			} else {
				Formations.setDef(of, squad, altBox.getSelectedIndex(), selected, 0);
			}
		} else if (new Ellipse2D.Double(21, 63, 14, 14).contains(e.getX(), e
				.getY())) {
			if (def == 2) {
				Formations.setDef(of, squad, altBox.getSelectedIndex(), selected, 1);
			} else {
				Formations.setDef(of, squad, altBox.getSelectedIndex(), selected, 2);
			}
		} else {
			for (int j = 0; j < 8; j++) {
				if (Formations.getAtk(of, squad, altBox.getSelectedIndex(), selected, j)) {
					count++;
				}
			}
			// System.out.println(count);
			for (int i = 0; boo & i < 8; i++) {
				if (attSqu[i].contains(e.getX(), e.getY())) {
					boo = false;
					if (Formations.getAtk(of, squad, altBox.getSelectedIndex(), selected, i)) {
						//of.data[a + (altBox.getSelectedIndex() * 171)] = 0;
						Formations.setAtk(of, squad, altBox.getSelectedIndex(), selected, i);
					} else if (count < 2) {
						if (selected != 0) {
							Formations.setAtk(of, squad, altBox.getSelectedIndex(), selected, i);
							//of.data[a + (altBox.getSelectedIndex() * 171)] = 1;
						} else if (i == 0 || i == 4) {
							Formations.setAtk(of, squad, altBox.getSelectedIndex(), selected, i);
							//of.data[a + (altBox.getSelectedIndex() * 171)] = 1;
						}
					}
				}
			}
		}
		repaint();
		pitch.repaint();
	}

	public void mouseReleased(MouseEvent e) {
	};

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

}
