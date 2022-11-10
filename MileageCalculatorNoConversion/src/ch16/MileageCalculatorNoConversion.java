/**
 * File: csci1302/ch16/MileageCalculator.java
 * Package: ch16
 * @author Christopher Williams
 * Created on: Apr 12, 2017
 * Last Modified: Apr 15, 2019
 * Description:  
 */
package ch16;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MileageCalculatorNoConversion extends Application {
	// default values/strings
    private double txtWidth = 125.0;
    private String defaultCalc = String.format("%.2f", 0.00);
    private String defaultEntry = String.format("%.2f", 0.00);
    private String defaultMileage = "Miles";
    private String defaultCapacity = "Gallons";
    private String defaultResult = "MPG";
    private String altMileage = "Kilometers";
    private String altCapacity = "Liters";
    private String altResult = "L/100KM";
    
    // create UI components split by type
    private Button btnCalc = new Button("Calculate");
    private Button btnReset = new Button("Reset");
    
    private Label lblDistance = new Label(defaultMileage);
    private Label lblCapacity = new Label(defaultCapacity);
    private Label lblResult = new Label(defaultResult);
    private Label lblEffType = new Label("Efficiency Type");
    private Label lblUnits = new Label("Units");
    
    private TextField tfDistance = new TextField(defaultEntry);
    private TextField tfCapacity = new TextField(defaultEntry);
    private TextField tfResult = new TextField(defaultCalc);    
    
    private GridPane mainPane = new GridPane();
    
    private ComboBox<String> toggleConvBox = new ComboBox<>();
    
    public String getUnits() {
    	return this.toggleConvBox.getSelectionModel().getSelectedItem();
    }
    public void start(Stage primaryStage) {   	
    	// set toggle group for RadioButtons

    	toggleConvBox.getItems().addAll(defaultMileage,altMileage);
	toggleConvBox.getSelectionModel().selectedItemProperty().addListener((e)->changeLabels());

    	toggleConvBox.getSelectionModel().select(0);
        // set preferences for UI components
        tfDistance.setMaxWidth(txtWidth);
        tfCapacity.setMaxWidth(txtWidth);
        tfResult.setMaxWidth(txtWidth);
        tfResult.setEditable(false);
        
        // create a main grid pane to hold items
        mainPane.setPadding(new Insets(10.0));
        mainPane.setHgap(txtWidth/2.0);
        mainPane.setVgap(txtWidth/12.0);
        
        // add items to mainPane
        mainPane.add(lblEffType, 0, 0);
        
        mainPane.add(lblDistance, 0, 2);
        mainPane.add(tfDistance, 1, 2);
        mainPane.add(lblCapacity, 0, 3);
        mainPane.add(tfCapacity, 1, 3);
        mainPane.add(lblResult, 0, 4);
        mainPane.add(tfResult, 1, 4);
        mainPane.add(btnReset, 0, 5);
        mainPane.add(btnCalc, 1, 5);
        
        mainPane.add(lblUnits, 0, 1);
        mainPane.add(toggleConvBox, 1, 1);
        
        // register action handlers
        btnCalc.setOnAction(e -> calcMileage());
        tfDistance.setOnAction(e -> calcMileage());
        tfCapacity.setOnAction(e -> calcMileage());
        tfResult.setOnAction(e -> calcMileage());
       
        btnReset.setOnAction(e -> resetForm());
        // create a scene and place it in the stage
        Scene scene = new Scene(mainPane); 
        
        // set and show stage
        primaryStage.setTitle("Mileage Calculator"); 
        primaryStage.setScene(scene); 
        primaryStage.show();      
        
        // stick default focus in first field for usability
        tfDistance.requestFocus();
    }
        
    /**
     * Convert existing figures and recalculate
     * This needs to be separate to avoid converting when
     * the conversion is not necessary
     */
    
   
    private final double distanceFactor = 1.60934;
    private double toKilometers(double miles) {
    	return miles * distanceFactor;
    }
    private double toMiles(double kilometers) {
    	return kilometers * (1/distanceFactor);
    }
    private final double volumeFactor = 3.78541;
    private double toLiters(double gallons) {
    	return gallons * volumeFactor;
    }
    private double toGallons(double liters) {
    	return liters * (1/volumeFactor);
    }
    
    private void changeLabels() {
    	// distinguish between L/100KM and MPG    	
    	String fromUnits = lblDistance.getText();
    	String toUnits = getUnits();
    	if(toUnits.equals(fromUnits)) {
    		//do nothing
    		return;
    	}
    	if(getUnits()==defaultMileage) {
    		lblCapacity.setText(defaultCapacity);
        	lblDistance.setText(defaultMileage);
        	lblResult.setText(defaultResult);
    	}
    	else
    	{
    		lblCapacity.setText(altCapacity);
        	lblDistance.setText(altMileage);
        	lblResult.setText(altResult);   
    	}  
    	double currentValueD = Double.valueOf(tfDistance.getText());
    	double currentValueC = Double.valueOf(tfCapacity.getText());
    	if(fromUnits.equals(defaultMileage)) {
    		tfDistance.setText(String.format("%.2f", toKilometers(currentValueD)));
    		tfCapacity.setText(String.format("%.2f", toLiters(currentValueC)));
    	}
    	else
    	{
    		tfDistance.setText(String.format("%.2f", toMiles(currentValueD)));
    		tfCapacity.setText(String.format("%.2f", toGallons(currentValueC)));
    	}    	
    	calcMileage();
    }
    
    /**
     * Calculate expenses based on entered figures
     */
    private void calcMileage() {       
    	// set default values
        double distance = 0.0, capacity = 0.0;
        

        // make sure to get numeric values only
        if (tfCapacity.getText() != null && !tfCapacity.getText().isEmpty()
        		&& tfDistance.getText() != null && !tfDistance.getText().isEmpty()) {
        	distance = Double.parseDouble(tfDistance.getText());
            capacity = Double.parseDouble(tfCapacity.getText());
        }
        
        double result = 0;
        // check for type of calculation
        
        
        
           if (getUnits()==defaultMileage) {
        	// liters / 100KM
        	result = (distance != 0) ? capacity/(distance/100.0) : 0;
        } else {
        	// MPG
        	result = (capacity != 0) ? distance/capacity : 0;       	
        }
    	
        
        
	    // update calculation fields with currency formatting
        tfResult.setText(String.format("%.2f", result));
    }
    
    /**
     * Reset all values in the application
     */
    private void resetForm() {
        // reset all form fields
        tfDistance.setText(defaultEntry);
        tfCapacity.setText(defaultEntry);
        tfResult.setText(defaultCalc);
        lblCapacity.setText(defaultCapacity);
    	lblDistance.setText(defaultMileage);
    	lblResult.setText(defaultResult);
    	toggleConvBox.getSelectionModel().select(0);
    	this.changeLabels();
    }
	
	
	public static void main(String[] args) {
		launch(args);
	}

}

