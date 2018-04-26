/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.gwtboot.sample.ui.domino.client.ui;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.button.IconButton;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.forms.TextArea;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.layout.Layout;
import org.dominokit.domino.ui.lists.ListGroup;
import org.dominokit.domino.ui.lists.ListItem;
import org.dominokit.domino.ui.popover.Tooltip;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.themes.Theme;

import elemental2.dom.Event;

import static com.github.gwtboot.sample.ui.domino.client.ui.HelloWorldClientBundle.BUNDLE;
import static com.github.gwtboot.sample.ui.domino.client.ui.HelloWorldClientBundle.CONSTANTS;

@Singleton
public class HelloWorldView {

	private static Logger logger = Logger
			.getLogger(HelloWorldView.class.getName());

	TextBox titleTextBox;

	TextArea descriptionTextArea;

	ListGroup<TodoItem> todoItemsListGroup;

	ListGroup<TodoItem> doneItemsListGroup;

	Button addButton;

	@Inject
	public HelloWorldView() {
		logger.info("Create HelloWorldView");

		// Input fields
		titleTextBox = TextBox.create(CONSTANTS.title()).floating();
		descriptionTextArea = TextArea.create(CONSTANTS.description())
				.floating().setRows(1);

		// Groups todoList and doneList
		todoItemsListGroup = ListGroup.create();
		doneItemsListGroup = ListGroup.create();

		// Add button and listener
		addButton = Button.createPrimary(CONSTANTS.add());
		addButton.asElement().classList.add(BUNDLE.css().addButton());
		addButton.addClickListener(addButtonClickEvent -> {
			handleAddButtonClick(addButtonClickEvent);
		});

		// Layout
		createLayout();
	}

	private void createLayout() {
		Layout layout = Layout.create(CONSTANTS.appTitle()).removeLeftPanel()
				.show(Theme.BLUE);
		layout.getContentPanel().appendChild(
				Card.create(CONSTANTS.new_todo(), CONSTANTS.add_new_todo())
						.appendContent(titleTextBox.asElement())
						.appendContent(descriptionTextArea.asElement())
						.appendContent(addButton.asElement()).asElement());
		layout.getContentPanel().appendChild(Card.create(CONSTANTS.todo_items())
				.appendContent(todoItemsListGroup.asElement()).asElement());
		layout.getContentPanel().appendChild(Card.create(CONSTANTS.done_items())
				.appendContent(doneItemsListGroup.asElement()).asElement());
	}

	private void handleAddButtonClick(Event addButtonClickEvent) {
		// We click the addButton
		if (!titleTextBox.isEmpty() && !descriptionTextArea.isEmpty()) {
			// Create a new todoItem
			TodoItem todoItem = new TodoItem(titleTextBox.getValue(),
					descriptionTextArea.getValue());

			ListItem<TodoItem> listItem = todoItemsListGroup
					.createItem(todoItem, todoItem.getDescription())
					.setHeading(todoItem.getTitle());

			// Done button and listener
			IconButton doneButton = IconButton.create(Icons.ALL.check());
			doneButton.setButtonType(StyleType.SUCCESS);
			doneButton.asElement().classList.add(BUNDLE.css().doneButton());
			doneButton.addClickListener(doneButtonClickEvent -> {
				handleDoneButtonClick(doneButtonClickEvent, doneButton,
						listItem);
			});

			listItem.appendContent(doneButton.asElement());

			todoItemsListGroup.appendItem(listItem);

			Tooltip.create(doneButton.asElement(), CONSTANTS.mark_done());

			// Clear input fields
			titleTextBox.setValue("");
			descriptionTextArea.setValue("");
		}
	}

	private void handleDoneButtonClick(Event doneButtonClickEvent,
			IconButton doneButton, ListItem<TodoItem> listItem) {
		// We click the doneButton
		doneButtonClickEvent.stopPropagation();

		todoItemsListGroup.removeItem(listItem);
		doneItemsListGroup.appendItem(listItem);

		doneButton.asElement().remove();
	}

}
