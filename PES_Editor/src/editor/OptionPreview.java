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

import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

public class OptionPreview extends JPanel implements PropertyChangeListener
{
	private File file = null;

	private XportInfo xpInfo = new XportInfo();

	private JTextArea previewText = new JTextArea(20, 19); // 30

	private FileFilter filter;

	public OptionPreview(JFileChooser fc)
	{
		super();
		filter = fc.getFileFilter();
		setBorder(BorderFactory.createTitledBorder("Details"));
		previewText.setFont(new Font("SansSerif", Font.PLAIN, 12));
		previewText.setEditable(false);
		previewText.setLineWrap(true);
		previewText.setWrapStyleWord(true);
		fc.addPropertyChangeListener(this);
		add(previewText);
	}

	public void loadImage()
	{
		if (file == null || file.isDirectory() || !(filter.accept(file)))
		{
			previewText.setText("");
			return;
		}
		String extension = PESUtils.getExtension(file);
		if (extension != null)
		{

			if (xpInfo.getInfo(file))
			{
				if (extension.equals(PESUtils.xps))
				{
					previewText.setText(xpInfo.game + "\n\nX-Port game name:\n" +
						xpInfo.gameName + "\n\nX-Port save name:\n" +
						xpInfo.saveName + "\n\nX-Port notes:\n" +
						xpInfo.notes);
				}
				else if (extension.equals(PESUtils.max))
				{
					previewText.setText(xpInfo.game + "\n\n" + xpInfo.gameName);
				}
				else
				{
					previewText.setText(xpInfo.game);
				}
			}
		}
		else
		{
			previewText.setText("PC");
		}
	}

	public void propertyChange(PropertyChangeEvent e)
	{
		boolean update = false;
		String prop = e.getPropertyName();
		if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop))
		{
			file = null;
			update = true;
		}
		else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop))
		{
			file = (File) e.getNewValue();
			update = true;
		}
		if (update)
		{
			previewText.setText("");
			if (isShowing())
			{
				loadImage();
				repaint();
			}
		}
	}
}
