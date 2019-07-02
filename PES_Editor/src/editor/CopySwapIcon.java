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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.Icon;
import javax.swing.SwingConstants;

public class CopySwapIcon implements Icon, SwingConstants {
	private boolean swap = false;

	private int width = 10;

	private int height = 20;

	public CopySwapIcon(boolean s) {
		swap = s;
	}

	public int getIconHeight() {
		return height;
	}

	public int getIconWidth() {
		return width;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(x, y);
		g2.setPaint(Color.black);
		g2.draw(new Line2D.Double(5, 0, 5, 20));

		g2.draw(new Line2D.Double(5, 20, 0, 15));
		g2.draw(new Line2D.Double(5, 20, 10, 15));
		if (swap) {
			g2.draw(new Line2D.Double(5, 0, 0, 5));
			g2.draw(new Line2D.Double(5, 0, 10, 5));
		}

		g2.translate(-x, -y); // Restore graphics object
	}
}
