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

import java.awt.GridLayout;

import javax.swing.JPanel;

public class WENShopPanel extends JPanel {
	WENPanel wenPanel;

	ShopPanel shopPanel;

	public WENShopPanel(OptionFile opf) {
		super();
		JPanel pan = new JPanel(new GridLayout(0, 1));
		wenPanel = new WENPanel(opf);
		shopPanel = new ShopPanel(opf);
		pan.add(wenPanel);
		pan.add(shopPanel);
		add(pan);
	}

}
