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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpDialog extends JDialog
{
	private JEditorPane editpane;

	private URL helpURL;

	public HelpDialog(Frame owner)
	{
		super(owner, "PES Editor Help", false);
		JScrollPane scroll = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// try {
		// helpURL = f.toURL();
		helpURL = Editor.class.getResource("help/index.html");
		// System.out.println(helpURL);
		// } catch (MalformedURLException e) {
		// }
		JButton exitButton = new JButton("Close Help");
		exitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent a)
			{
				setVisible(false);
			}
		});
		editpane = new JEditorPane();
		editpane.setEditable(false);
		editpane.addHyperlinkListener(new MyHyperlinkListener());
		scroll.setViewportView(editpane);
		showPage();
		scroll.setPreferredSize(new Dimension(430, 550));
		getContentPane().add(scroll, BorderLayout.CENTER);
		getContentPane().add(exitButton, BorderLayout.SOUTH);
		pack();
		// setResizable(false);
	}

	public void showPage()
	{
		if (helpURL != null)
		{
			try
			{
				editpane.setPage(helpURL);
			}
			catch (java.io.IOException ioe)
			{
				System.out.println("IOExecption while loading help page.");
			}
		}
		else
		{
			System.out.println("null.");
		}
	}

	private class MyHyperlinkListener implements HyperlinkListener
	{
		public void hyperlinkUpdate(HyperlinkEvent evt)
		{
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			{
				try
				{
					// Show the new page in the editor pane.
					editpane.setPage(evt.getURL());

				}
				catch (Exception e)
				{
					// e.printStackTrace();
				}
			}
		}
	}

}
