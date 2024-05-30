package application;

import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Calculator extends Application {

	private TextField textField = new TextField();
	private boolean start = true;
	private boolean newOperation = false;
	private String lastResult = "";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {		
		textField.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		textField.setAlignment(Pos.CENTER_RIGHT);
		textField.setPadding(new Insets(5));
		textField.setPrefHeight(80);
		textField.setEditable(false);

		StackPane stackPane = new StackPane();
		stackPane.setPadding(new Insets(10));
		stackPane.getChildren().add(textField);

		FlowPane pane = new FlowPane();
		pane.setHgap(10);
		pane.setVgap(5);
		pane.setAlignment(Pos.TOP_CENTER);

		pane.getChildren().add(createButtonForClear());
		pane.getChildren().add(createButtonForBack());
		pane.getChildren().add(createButtonForOperator("/"));

		pane.getChildren().add(createButtonForNumber("7"));
		pane.getChildren().add(createButtonForNumber("8"));
		pane.getChildren().add(createButtonForNumber("9"));
		pane.getChildren().add(createButtonForOperator("x"));

		pane.getChildren().add(createButtonForNumber("4"));
		pane.getChildren().add(createButtonForNumber("5"));
		pane.getChildren().add(createButtonForNumber("6"));
		pane.getChildren().add(createButtonForOperator("-"));

		pane.getChildren().add(createButtonForNumber("1"));
		pane.getChildren().add(createButtonForNumber("2"));
		pane.getChildren().add(createButtonForNumber("3"));
		pane.getChildren().add(createButtonForOperator("+"));

		pane.getChildren().add(createButtonForNumber("."));
		pane.getChildren().add(createButtonForNumber("0"));
		pane.getChildren().add(createButtonForOperator2("="));

		BorderPane root = new BorderPane();
		root.setTop(stackPane);
		root.setCenter(pane);
		Scene scene = new Scene(root, 290, 420);
		scene.setCursor(Cursor.HAND);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Calculator");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private Button createButtonForNumber(String ch) {
		Button button = new Button(ch);
		button.setPrefSize(60, 50);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		button.setOnAction(this::processNumber);
		return button;
	}

	private Button createButtonForOperator(String ch) {
		Button button = new Button(ch);
		button.setPrefSize(60, 50);
		button.setTextFill(Color.GOLDENROD);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		button.setOnAction(this::processOperator);
		return button;
	}

	private Button createButtonForOperator2(String ch) {
		Button button = new Button(ch);
		button.setPrefSize(130, 50);
		button.setTextFill(Color.GOLDENROD);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		button.setOnAction(this::processOperator);
		return button;
	}

	private Button createButtonForClear() {
		Button button = new Button("C");
		button.setPrefSize(130, 54);
		button.setTextFill(Color.RED);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		button.setOnAction(e -> {
			textField.setText("0");
			start = true;
			newOperation = false;
			lastResult = "";
		});
		return button;
	}

	private Button createButtonForBack() {
		Button button = new Button("DEL");
		button.setPrefSize(60, 54);
		button.setTextFill(Color.RED);
		button.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		button.setOnAction(e -> {
			String text = textField.getText();
			if (text.length()>1) {
				text = text.substring(0, text.length() - 1);
				textField.setText(text);
			}
			else {
				textField.setText("0");
				start=true;
			}
		});
		return button;
	}

	private void processNumber(ActionEvent e) {
		Button button = (Button) e.getSource();
		String value = button.getText();
		
		boolean PointExists=false;
		if(value==".") {
			if (start==true || newOperation==true) {
				textField.setText("0");
				start = false;
				newOperation = false;
			}
			String exp=textField.getText();
			for(int i=exp.length()-1;i>=0;i--) {
				char ch=exp.charAt(i);
				if(ch=='.') {
					PointExists=true;
				}
				else if(Character.isDigit(ch)) {
					continue;
				}
				else {
					break;
				}
			}
		}
		else {
			if (start==true || newOperation==true) {
				textField.setText("");
				start = false;
				newOperation = false;
			}
		}
		if(PointExists==true) {
			return;
		}
		else {
			textField.setText(textField.getText() + value);
		}
	}

	private void processOperator(ActionEvent e) {
		
		Button button = (Button) e.getSource();
		String value = button.getText();
		
		if (value != "=") {
			String text=textField.getText();
			if(isOperator(text.charAt(text.length()-1))) {
				text=text.substring(0,text.length()-1);
				textField.setText(text);
			}
			if (newOperation==true) {
				textField.setText(lastResult + value);
				newOperation = false;
			} else {
				textField.setText(textField.getText() + value);
			}
		} else {
			// calculate the expression
			String expression = textField.getText();
			String result = evaluateExpression(rightExpression(expression));
			textField.setText(result);
			lastResult = result;
			newOperation = true;
		}
	}
	
	private String rightExpression(String exp) {
		if(isOperator(exp.charAt(exp.length()-1))) {
			exp=exp.substring(0,exp.length()-1);
		}
		return exp;
	}
	
	private boolean isOperator(char ch) {
		if(ch=='+'||ch=='-'||ch=='x'||ch=='/')
			return true;
		else
			return false;
	}

	public static int precedence(char op) {
		if (op == '+' || op == '-')
			return 1;
		if (op == 'x' || op == '/')
			return 2;
		return 0;
	}

	public static double applyOp(double a, double b, char op) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case 'x':
			return a * b;
		case '/':
			return a / b;
		}
		return 0.0;
	}

	public static String evaluateExpression(String expression) {
		Stack<Double> values = new Stack<>();
		Stack<Character> ops = new Stack<>();

		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == ' ')
				continue;

			if (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.') {
				StringBuilder sb = new StringBuilder();
				while (i < expression.length()
						&& (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
					sb.append(expression.charAt(i));
					i++;
				}
				i--;
				values.push(Double.parseDouble(sb.toString()));
			} else if (expression.charAt(i) == '-' && (i == 0 || !Character.isDigit(expression.charAt(i - 1)))) {
				StringBuilder sb = new StringBuilder();
				sb.append('-');
				i++;
				while (i < expression.length()
						&& (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
					sb.append(expression.charAt(i));
					i++;
				}
				i--;
				values.push(Double.parseDouble(sb.toString()));
			} else {
				while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(expression.charAt(i))) {
					double b = values.pop();
					double a = values.pop();
					char op = ops.pop();
					values.push(applyOp(a, b, op));
				}
				ops.push(expression.charAt(i));
			}
		}

		while (!ops.isEmpty()) {
			double b = values.pop();
			double a = values.pop();
			char op = ops.pop();
			values.push(applyOp(a, b, op));
		}

		return String.valueOf(values.pop());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}