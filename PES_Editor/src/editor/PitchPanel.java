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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class PitchPanel extends JPanel implements MouseListener, MouseMotionListener
{
	OptionFile of;

	SquadList list;

	AttDefPanel adPanel;

	int squad = 0;

	int selected = -1;

	boolean attack = true;

	boolean defence = true;

	boolean numbers = true;

	boolean roleOn = true;

	int adj = 14;

	Color[] colour = { new Color(0, 0, 0), new Color(255, 255, 255),
			new Color(255, 255, 0), new Color(0, 255, 255),
			new Color(0, 255, 0), new Color(255, 0, 0), new Color(0, 0, 255),
			Color.gray };

	JComboBox altBox;

	SquadNumList numList;

	int xadj = 0;

	int yadj = 0;

	public PitchPanel(OptionFile opf, SquadList fsl, AttDefPanel adp, JComboBox ab, SquadNumList nl)
	{
		super();
		of = opf;
		list = fsl;
		adPanel = adp;
		altBox = ab;
		numList = nl;
		setOpaque(true);
		setPreferredSize(new Dimension(329 + (adj * 2), 200 + (adj * 2)));
		setBackground(Color.black);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.black);
		g2.fill(new Rectangle2D.Double(0, 0, 329 + (adj * 2), 200 + (adj * 2)));
		g2.setPaint(Color.gray);
		g2.draw(new Rectangle2D.Double(13, 13, 329 + 2, 200 + 2));
		g2.draw(new Line2D.Double(178, 13, 178, 215));
		g2.draw(new Ellipse2D.Double(178 - 33, 114 - 33, 66, 66));
		g2.draw(new Rectangle2D.Double(13, 62, 46, 104));
		g2.draw(new Rectangle2D.Double(298, 62, 46, 104));
		g2.draw(new Rectangle2D.Double(13, 85, 17, 58));
		g2.draw(new Rectangle2D.Double(327, 85, 17, 58));
		g2.draw(new Arc2D.Double(40, 89, 38, 49, 270, 180, Arc2D.OPEN));
		g2.draw(new Arc2D.Double(279, 89, 38, 49, 90, 180, Arc2D.OPEN));
		int x;
		int y;
		int pos;
		
		for (int p = 0; p < 11; p++)
		{
			pos = Formations.getPos(of, squad, altBox.getSelectedIndex(), p);
			// System.out.println(pos);
			if (p == 0)
			{
				x = 0 + adj;
				y = 90 + adj;
				// g2.setPaint(Color.yellow);
			}
			else 
			{
				x = ((Formations.getX(of, squad, altBox.getSelectedIndex(), p) - 2) * 7) + adj;
				y = ((Formations.getY(of, squad, altBox.getSelectedIndex(), p) - 6) * 2) + adj;
			}
			if (p == selected)
				g2.setPaint(Color.white);
			else
			{
				if (pos == 0)
					g2.setPaint(Color.yellow);
				else if (pos > 0 && pos < 10)
					g2.setPaint(Color.cyan);
				else if (pos > 9 && pos < 29)
					g2.setPaint(Color.green);
				else if (pos > 28 && pos < 41)
					g2.setPaint(Color.red);
			}
			g2.fill(new Ellipse2D.Double(x, y, 14, 14));

			// draw position label
			if (roleOn)
			{
				g2.setFont(new Font("Dialog", Font.BOLD, 10));
				int adjx = 0;
				if (pos == 30 || pos == 16 || pos == 4)
					adjx = -1;

				boolean up = Formations.getAtk(of, squad, altBox.getSelectedIndex(), p, 2);
				boolean down = Formations.getAtk(of, squad, altBox.getSelectedIndex(), p, 6);
				if (up && down)
				{
					g2.drawString(getPosLabel(pos).substring(0, 1), x + 15, y + 6);
					g2.drawString(getPosLabel(pos).substring(1, 2), x + 15, y + 16);
				}
				else if (pos == 9 || pos == 16 || pos == 23 || pos == 30)
				{
					if (!down)
					{
						g2.drawString(getPosLabel(pos), x + adjx, y + 24);
					}
					else
					{
						g2.drawString(getPosLabel(pos), x + adjx, y - 2);
					}
				}
				else
				{
					if (up)
						g2.drawString(getPosLabel(pos), x + adjx, y + 24);
					else
						g2.drawString(getPosLabel(pos), x + adjx, y - 2);
				}
			}

			if (attack) {
				int x1 = x + 7;
				int y1 = y + 7;
				int x2 = x1;
				int y2 = y1;
				for (int i = 0; i < 8; i++)
				{
					if (Formations.getAtk(of, squad, altBox.getSelectedIndex(),p, i))
					{
						switch (i)
						{
							case 0:
								x2 = x1 - 21;
								y2 = y1;
								break;
							case 1:
								x2 = x1 - 15;
								y2 = y1 - 15;
								break;
							case 2:
								x2 = x1;
								y2 = y1 - 21;
								break;
							case 3:
								x2 = x1 + 15;
								y2 = y1 - 15;
								break;
							case 4:
								x2 = x1 + 21;
								y2 = y1;
								break;
							case 5:
								x2 = x1 + 15;
								y2 = y1 + 15;
								break;
							case 6:
								x2 = x1;
								y2 = y1 + 21;
								break;
							case 7:
								x2 = x1 - 15;
								y2 = y1 + 15;
								break;
						}
						g2.draw(new Line2D.Double(x1, y1, x2, y2));
					}
				}
			}

			if (numbers)
			{
				g2.setFont(new Font("Dialog", Font.BOLD, 10));
				g2.setPaint(Color.black);
				String numText = (String) numList.getModel().getElementAt(p);
				int ta = 0;
				if (numText.length() == 1)
					ta = 3;
				if (numText.startsWith("1"))
					ta = ta - 1;
				g2.drawString(numText, x + 2 + ta, y + 11);
			}

			if (defence)
			{
				g2.setPaint(Color.blue);
				int size = 6;
				int x1 = (x + 7) - 13 - (size / 2);
				int y1 = (y + 7) - 5 - (size / 2);
				int x2 = (x + 7) - 13 - (size / 2);
				int y2 = (y + 7) + 5 - (size / 2);
				if (Formations.getDef(of, squad, altBox.getSelectedIndex(), p) == 1)
					g2.fill(new Ellipse2D.Double(x2, y2, size, size));
				else if (Formations.getDef(of, squad, altBox.getSelectedIndex(), p) == 0)
				{
					g2.fill(new Ellipse2D.Double(x1, y1, size, size));
					g2.fill(new Ellipse2D.Double(x2, y2, size, size));
				}
			}
		}
	}

	public void mousePressed(MouseEvent e)
	{
		int x;
		int y;
		Ellipse2D.Double circle;
		selected = -1;
		// Checks whether or not the cursor is inside of the rectangle
		// while the user is pressing the mouse.
		for (int i = 1; selected == -1 && i < 11; i++)
		{
			x = ((Formations.getX(of, squad, altBox.getSelectedIndex(), i) - 2) * 7) + adj;
			y = ((Formations.getY(of, squad, altBox.getSelectedIndex(), i) - 6) * 2) + adj;
			circle = new Ellipse2D.Double(x, y, 14, 14);
			if (circle.contains(e.getX(), e.getY()))
			{
				selected = i;
				xadj = e.getX() - x;
				yadj = e.getY() - y;
				// System.out.println(of.data[670641 + (628 * squad) + 6232 +
				// selected + (altBox.getSelectedIndex() * 171)]);
			}
		}
		FormPanel.fromPitch = true;
		if (selected != -1)
		{
			list.setSelectedIndex(selected);
			adPanel.selected = selected;
		}
		else 
		{
			circle = new Ellipse2D.Double(0 + adj, 90 + adj, 14, 14);
			if (circle.contains(e.getX(), e.getY()))
			{
				selected = 0;
				list.setSelectedIndex(selected);
				adPanel.selected = selected;
			}
			else
			{
				list.clearSelection();
				adPanel.selected = -1;
			}
		}
		repaint();
		adPanel.repaint();
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseDragged(MouseEvent e)
	{
		if (selected > 0)
		{
			int x = e.getX() - xadj;
			int y = e.getY() - yadj;
			int pos = Formations.getPos(of, squad, altBox.getSelectedIndex(), selected);
			if (x < 0 + adj)
				x = 0 + adj;
			if (y < 0 + adj)
				y = 0 + adj;
			if (x > 315 + adj)
				x = 315 + adj;
			if (y > 186 + adj)
				y = 186 + adj;

			x = ((x - adj) / 7) + 2;
			y = ((y - adj) / 2) + 6;

			if (pos > 0 && pos < 10)
			{
				if (x > 15)
					x = 15;
			}

			else if (pos > 9 && pos < 29)
			{
				if (x < 16)
					x = 16;
				else if (x > 34)
					x = 34;
			}
			else if (pos > 28 && pos < 41)
			{
				if (x < 35)
					x = 35;
			}

			if (pos == 8 || pos == 15 || pos == 22 || pos == 29) {
				if (y > 50)
					y = 50;
			}

			if (pos == 9 || pos == 16 || pos == 23 || pos == 30) {
				if (y < 54)
					y = 54;
			}

			// System.out.println(x + ", " + y);
			Formations.setX(of, squad, altBox.getSelectedIndex(), selected, x);
			Formations.setY(of, squad, altBox.getSelectedIndex(), selected, y);
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {}

	private String getPosLabel(int p)
	{
		String label = "";
		if (p == 0)
			label = "GK";
		if ((p > 0 && p < 4) || (p > 4 && p < 8))
			label = "CB";
		if (p == 4)
			label = "CW";
		if (p == 8)
			label = "LB";
		if (p == 9)
			label = "RB";
		if (p > 9 && p < 15)
			label = "DM";
		if (p == 15)
			label = "LW";
		if (p == 16)
			label = "RW";

		if (p > 16 && p < 22)
			label = "CM";
		if (p == 22)
			label = "LM";
		if (p == 23)
			label = "RM";
		if (p > 23 && p < 29)
			label = "AM";
		if (p == 29)
			label = "LW";
		if (p == 30)
			label = "RW";
		if (p > 30 && p < 36)
			label = "SS";

		if (p > 35 && p < 41)
			label = "CF";

		if (p > 40)
			label = String.valueOf(p);
		return label;
	}

}
