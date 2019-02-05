//package edu.cpp.CS2450;

import java.io.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
/**
 *
 * @author Varoozhan Hartoonian, Sarmen Andreasian, Jesus Leon, Chanwoo Kim
 * Let's put a more detailed comment on what was done on the project here:
 * Varoozhan & Sarmen: So far we have added the shape change color tool
 * Jesus: Added save and load functionalities
 *
 */

public class HW4 extends Application
{
    PhongMaterial materialBox;
    PhongMaterial materialCylinder;
    PhongMaterial materialSphere;
    PhongMaterial material;
    Rotate rotateBoxX;
    Rotate rotateBoxY;
    Rotate rotateCylX;
    Rotate rotateCylY;
    Rotate rotateSphX;
    Rotate rotateSphY;
    Rotate rotateX;
    Rotate rotateY;
    Scale boxScale;
    Scale cylScale;
    Scale sphScale;
    Scale scale;
    Translate boxTranslate;
    Translate cylTranslate;
    Translate sphTranslate;
    Translate translate;
    ChoiceBox<String> shapeChoice;
    Button addShapeAddButton;
    TextField radiusTextField;
    TextField widthTextField;
    TextField heightTextField;
    TextField lengthTextField;
    Group overallGroup = new Group();
    Group shapesGroup;
    Stage primaryStage;
    Scene scene;
    Shape3D clickedShape;
    SubScene shapesSubScene;
    Color subColor = Color.WHITE;


    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage)
    {
        BorderPane borderPane = new BorderPane();

        MenuBar menuB = new MenuBar();
        Menu menu = new Menu("File");
        menuB.getMenus().add(menu);
        MenuItem save = new MenuItem("Save");
        MenuItem open = new MenuItem("Open");
        menu.getItems().addAll(save,new SeparatorMenuItem(),open);

        save.setOnAction(event -> {
            try {
                saveFile();
            } catch (Exception ex) {
                System.out.println("Error on Save");
            }
        });

        open.setOnAction(event -> {
            try {
                loadFile();
            } catch (Exception ex) {
                System.out.println("Error on Load");
            }
        });

        VBox rootNode = new VBox(10);
        rootNode.setAlignment(Pos.CENTER);
        shapesGroup = new Group(overallGroup.getChildren());
        //shapesGroup.getChildren().addAll(sphere,cylinder);
        shapesSubScene = new SubScene(shapesGroup,900,600,true, SceneAntialiasing.DISABLED);
        Rotate hRotate = new Rotate(45, Rotate.X_AXIS);
        PerspectiveCamera pCamera = new PerspectiveCamera(true);
        pCamera.getTransforms().addAll(hRotate,new Translate(0,0,-60));
        shapesSubScene.setCamera(pCamera);
        shapesSubScene.setFill(subColor);



        Button addShapeButton = new Button("Add Shape");

        addShapeButton.setOnAction(event -> {
            overallGroup.getChildren().setAll(shapesGroup.getChildren());
            addShape(primaryStage);
        });

        Label backgroundLabel = new Label("Change Background Color: ");
        ChoiceBox<String> backgroundColorChoice = getColorChoiceBox();

        Button changeColor = new Button("Change Color");
        changeColor.setOnAction(event -> {
            String backgroundColorString = backgroundColorChoice.getSelectionModel().getSelectedItem();
            if(backgroundColorString == "RED")
            {
                shapesSubScene.setFill(Color.RED);
                subColor = Color.RED;
            }else if(backgroundColorString == "GREEN")
            {
                shapesSubScene.setFill(Color.GREEN);
                subColor = Color.GREEN;
            }else if(backgroundColorString == "BLUE")
            {
                shapesSubScene.setFill(Color.BLUE);
                subColor = Color.BLUE;
            }else if(backgroundColorString == "AZURE")
            {
                shapesSubScene.setFill(Color.AZURE);
                subColor = Color.AZURE;
            }else if(backgroundColorString == "GRAY")
            {
                shapesSubScene.setFill(Color.GRAY);
                subColor = Color.GRAY;
            }
        });

        HBox bottomHB = new HBox(10,backgroundLabel,backgroundColorChoice,changeColor);
        bottomHB.setAlignment(Pos.CENTER);
        VBox bottomVB = new VBox(10,addShapeButton,bottomHB);
        bottomVB.setAlignment(Pos.CENTER);
        bottomVB.setPadding(new Insets(10));

        /*
         * Varoohzan and Sarmen code starts here:
         */

// TOOLS: Change Color Part
        Label shapeColorLabel = new Label("Change shape color: ");
        ChoiceBox<String> shapeColorChoice = getColorChoiceBox();
        materialBox =new PhongMaterial();
        materialCylinder =new PhongMaterial();
        materialSphere =new PhongMaterial();
        material =new PhongMaterial();

        material = materialBox;

        shapeColorChoice.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                    String shapeColorString = shapeColorChoice.getSelectionModel().getSelectedItem();
                    if(shapeColorString == "RED")
                    {
                        material.setDiffuseColor(Color.RED);
                    }else if(shapeColorString == "GREEN")
                    {
                        material.setDiffuseColor(Color.GREEN);
                    }else if(shapeColorString == "BLUE")
                    {
                        material.setDiffuseColor(Color.BLUE);
                    }else if(shapeColorString == "AZURE")
                    {
                        material.setDiffuseColor(Color.AZURE);
                    }else if(shapeColorString == "GRAY")
                    {
                        material.setDiffuseColor(Color.GRAY);
                    }
                });

//TOOLS: Scale part
        GridPane gridPaneScale = new GridPane();
        gridPaneScale.setPadding(new Insets(10));
        Label scaleLabel=new Label("Please Enter Scale Size(default is 1): ");
        Label scaleWidth=new Label("Width: ");
        Label scaleHeight=new Label("Height: ");
        Label scaleDepth=new Label("Depth: ");
        TextField widthTextField = new TextField("1");
        TextField heightTextField = new TextField("1");
        TextField depthTextField = new TextField("1");

        gridPaneScale.add(scaleLabel,0,0);
        gridPaneScale.add(scaleWidth,0,1);
        gridPaneScale.add(scaleHeight,0,2);
        gridPaneScale.add(scaleDepth,0,3);
        gridPaneScale.add(widthTextField,1,1);
        gridPaneScale.add(heightTextField,1,2);
        gridPaneScale.add(depthTextField,1,3);

        boxScale = new Scale();
        cylScale = new Scale();
        sphScale = new Scale();
        scale = new Scale();
        scale = boxScale;

        widthTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            double widthValue = Double.parseDouble(widthTextField.getText());
            scale.setX(widthValue);
        });
        heightTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            double heightValue = Double.parseDouble(heightTextField.getText());
            scale.setY(heightValue);

        });
        depthTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            double depthValue = Double.parseDouble(depthTextField.getText());
            scale.setZ(depthValue);
        });

// TOOLS: Rotate part
        Label shapeRotateLabel = new Label("Rotate Shapes: ");
        Label xLabel = new Label("X-Axis Rotater");
        Label yLabel = new Label("Y-Axis Rotater");

        Slider xSlider = new Slider(0, 360, 0);
        xSlider.setPrefWidth(300.0);
        Slider ySlider = new Slider(0, 360, 0);
        ySlider.setPrefWidth(300.0);

        xSlider.setShowTickMarks(true);
        xSlider.setShowTickLabels(true);
        ySlider.setShowTickMarks(true);
        ySlider.setShowTickLabels(true);

        rotateBoxX = new Rotate(0, Rotate.X_AXIS);
        rotateBoxY = new Rotate(0, Rotate.Y_AXIS);
        rotateCylX = new Rotate(0, Rotate.X_AXIS);
        rotateCylY = new Rotate(0, Rotate.Y_AXIS);
        rotateSphX = new Rotate(0, Rotate.X_AXIS);
        rotateSphY = new Rotate(0, Rotate.Y_AXIS);

        xSlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            rotateX.pivotXProperty().set(translate.getX());
            rotateX.pivotYProperty().set(translate.getY());
            rotateX.pivotZProperty().set(translate.getZ());
            System.out.println(rotateX.getPivotX());
            rotateX.setAngle(xSlider.getValue());
        });

        ySlider.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            rotateY.pivotXProperty().set(translate.getX());
            rotateY.pivotYProperty().set(translate.getY());
            rotateY.pivotZProperty().set(translate.getZ());
            rotateY.setAngle(ySlider.getValue());
        });

        rotateX =rotateBoxX;
        rotateY =rotateBoxY;

// TOOLS: Translate part
        GridPane gridPaneTranslate = new GridPane();
        gridPaneScale.setPadding(new Insets(10));
        Label translateLabel=new Label("Please Enter x,y,z coordinates: ");
        Label translateX=new Label("X: ");
        Label translateY=new Label("Y: ");
        Label translateZ=new Label("Z: ");
        TextField xTextField = new TextField("0");
        TextField yTextField = new TextField("0");
        TextField zTextField = new TextField("0");

        gridPaneTranslate.add(translateLabel,0,0);
        gridPaneTranslate.add(translateX,0,1);
        gridPaneTranslate.add(translateY,0,2);
        gridPaneTranslate.add(translateZ,0,3);
        gridPaneTranslate.add(xTextField,1,1);
        gridPaneTranslate.add(yTextField,1,2);
        gridPaneTranslate.add(zTextField,1,3);

        boxTranslate = new Translate();
        cylTranslate = new Translate();
        sphTranslate = new Translate();
        translate = new Translate();
        translate = boxTranslate;

        xTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            double xValue = Double.parseDouble(xTextField.getText());
            translate.setX(xValue);
        });
        yTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            double yValue = Double.parseDouble(yTextField.getText());
            translate.setY(yValue);

        });
        zTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
            double zValue = Double.parseDouble(zTextField.getText());
            translate.setZ(zValue);
        });






        HBox toolHboxShapeColors = new HBox(10, shapeColorLabel,shapeColorChoice);
        toolHboxShapeColors.setPadding(new Insets(10));
        HBox toolHboxShapeRotate = new HBox(shapeRotateLabel);
        toolHboxShapeRotate.setAlignment(Pos.CENTER);
        HBox toolHboxShapeRotateSlider1 = new HBox(xLabel);
        toolHboxShapeRotateSlider1.setPadding(new Insets(5));
        HBox toolHboxShapeRotateSlider2 = new HBox(xSlider);
        toolHboxShapeRotateSlider2.setPadding(new Insets(10));
        HBox toolHboxShapeRotateSlider3 = new HBox(yLabel);
        toolHboxShapeRotateSlider3.setPadding(new Insets(5));
        HBox toolHboxShapeRotateSlider4 = new HBox(ySlider);
        toolHboxShapeRotateSlider4.setPadding(new Insets(10));
        VBox rightVB = new VBox(toolHboxShapeColors,toolHboxShapeRotate,toolHboxShapeRotateSlider1,toolHboxShapeRotateSlider2,
                toolHboxShapeRotateSlider3,toolHboxShapeRotateSlider4,gridPaneScale,gridPaneTranslate); //this vbox is for the tools part

        borderPane.setTop(menuB);
        borderPane.setCenter(shapesSubScene);
        //bP.setRight(); // VBOX of TOOLS
        borderPane.setBottom(bottomVB); // VBOX of 'Add Shape' Button and Background Color
        borderPane.setRight(rightVB);



        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Shape Application");
        primaryStage.show();
    }


    private void addShape(Stage addShapeStage) {
        Label shapeLabel = new Label("Select Shape: ");
        Label xLocation = new Label("x: ");
        Label yLocation = new Label("y: ");
        Label radius = new Label("radius: ");
        Label width = new Label("width: ");
        Label height = new Label("height: ");
        Label length = new Label("length: ");
        addShapeAddButton = new Button("Add");
        Button addShapeBackButton = new Button("Back");
        TextField xLocationTextField = new TextField("X Location");
        TextField yLocationTextField = new TextField("Y Location");
        radiusTextField = new TextField("width");
        widthTextField = new TextField("width");
        heightTextField = new TextField("height");
        lengthTextField = new TextField("length");

        shapeChoice = getShapeChoiceBox();

        addShapeAddButton.setOnAction(event->{
            String shapeS = shapeChoice.getSelectionModel().getSelectedItem();
            if(shapeS == "SPHERE")
            {
                overallGroup.getChildren().add(createSphere(radiusTextField.getText(), "default", "1", "1", "1",
                        "0", "0", xLocationTextField.getText(), yLocationTextField.getText(), "0"));
            }else if(shapeS == "BOX")
            {
                overallGroup.getChildren().add(createBox(widthTextField.getText(), heightTextField.getText(),
                        lengthTextField.getText(), "default", "1", "1", "1", "0", "0",
                        xLocationTextField.getText(), yLocationTextField.getText(), "0"));
            }else if(shapeS == "CYLINDER")
            {
                overallGroup.getChildren().add(createCylinder(heightTextField.getText(),
                        radiusTextField.getText(), "default", "1", "1", "1", "0", "0",
                        xLocationTextField.getText(), yLocationTextField.getText(), "0"));
            }
            start(addShapeStage);

        });



        GridPane addShapeGridPane = new GridPane();
        addShapeGridPane.setPadding(new Insets(10));

        addShapeGridPane.add(shapeLabel,0,0);
        addShapeGridPane.add(shapeChoice,1,0);
        addShapeGridPane.add(xLocation,0,1);
        addShapeGridPane.add(yLocation,0,2);
        addShapeGridPane.add(radius,0,3);
        addShapeGridPane.add(width,0,4);
        addShapeGridPane.add(height,0,5);
        addShapeGridPane.add(length,0,6);
        addShapeGridPane.add(addShapeAddButton,0,7);
        addShapeGridPane.add(xLocationTextField,1,1);
        addShapeGridPane.add(yLocationTextField,1,2);
        addShapeGridPane.add(radiusTextField,1,3);
        addShapeGridPane.add(widthTextField,1,4);
        addShapeGridPane.add(heightTextField,1,5);
        addShapeGridPane.add(lengthTextField,1,6);
        addShapeGridPane.add(addShapeBackButton,1,7);

        addShapeGridPane.setVgap(10);
        addShapeGridPane.setHgap(10);
        addShapeGridPane.setAlignment(Pos.CENTER);
        addShapeAddButton.setDisable(true);
        ChoiceBoxListener chListener =new  ChoiceBoxListener();
        shapeChoice.valueProperty().addListener(chListener);

        addShapeBackButton.setOnAction(event->{
            start(addShapeStage);
        });

        Scene scene = new Scene(addShapeGridPane,500,500);
        addShapeStage.setTitle("Add Shape");
        addShapeStage.setScene(scene);
        addShapeStage.show();

    }

    private class ChoiceBoxListener implements ChangeListener<String>
    {
        @Override
        public void changed(ObservableValue<? extends String> source, String oldValue, String
                newValue)
        {
            String shapeValue = shapeChoice.getValue();
            addShapeAddButton.setDisable(shapeValue.trim().equals(""));
            if(shapeValue.equals("SPHERE")) {
                lengthTextField.setDisable(true);
                radiusTextField.setDisable(false);
                widthTextField.setDisable(true);
                heightTextField.setDisable(true);
            }else if (shapeValue.equals("BOX")){
                lengthTextField.setDisable(false);
                radiusTextField.setDisable(true);
                widthTextField.setDisable(false);
                heightTextField.setDisable(false);
            } else {
                lengthTextField.setDisable(true);
                radiusTextField.setDisable(false);
                widthTextField.setDisable(true);
                heightTextField.setDisable(false);
            }
        }
    };

    ChoiceBox<String> getColorChoiceBox()
    {
        ChoiceBox<String> colorChoice = new ChoiceBox<>();
        colorChoice.getItems().addAll("RED","GREEN","BLUE","AZURE","GRAY");
        return colorChoice;
    }

    ChoiceBox<String> getShapeChoiceBox()
    {
        ChoiceBox<String> shapeChoice = new ChoiceBox<>();
        shapeChoice.getItems().addAll("SPHERE", "BOX", "CYLINDER");
        return shapeChoice;
    }

    //Handling for save and load by Jesus

    void saveFile() throws Exception{
        System.out.println("Save");
        ObservableList<Node> shapes = shapesGroup.getChildren();
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new ExtensionFilter("Shape Files", "*.shp"));
        File path = chooser.showSaveDialog(primaryStage);
        PrintWriter writer = new PrintWriter(path);
        writer.println(shapesSubScene.getFill().toString().substring(2));

        boolean gotScale;
        int gotRotate;
        boolean gotTranslate;

        for (int i = 0; i < shapes.size(); i++) {
            gotScale = false;
            gotRotate = 0;
            gotTranslate = false;

            writer.print(shapes.get(i).getClass().toString() + " ");

            if(shapes.get(i) instanceof Box) {
                writer.print(((Box)shapes.get(i)).getWidth() + " " +
                        ((Box)shapes.get(i)).getHeight() +  " " +
                        ((Box)shapes.get(i)).getDepth() + " ");

                Object color = ((Box)shapes.get(i)).getMaterial().toString();
                int beginningIndex = ((String)color).indexOf("diffuseColor=0x");
                color = ((String)color).subSequence(29, 35);

                writer.print(color + " ");

            }

            else if (shapes.get(i) instanceof Sphere){
                writer.print(((Sphere)shapes.get(i)).getRadius() + " ");

                Object color = ((Sphere)shapes.get(i)).getMaterial().toString();
                int beginningIndex = ((String)color).indexOf("diffuseColor=0x");
                color = ((String)color).subSequence(29, 35);

                writer.print(color + " ");
            }

            else {
                writer.print(((Cylinder)shapes.get(i)).getHeight() + " " + ((Cylinder)shapes.get(i)).getRadius() + " ");

                Object color = ((Cylinder)shapes.get(i)).getMaterial().toString();
                int beginningIndex = ((String)color).indexOf("diffuseColor=0x");
                color = ((String)color).subSequence(29, 35);

                writer.print(color + " ");
            }

            for(int j = 0; j < shapes.get(i).getTransforms().size(); j++) {

                if(shapes.get(i).getTransforms().get(j) instanceof Scale && gotScale == false) {
                    writer.print(((Scale)shapes.get(i).getTransforms().get(j)).getX() + " "
                            + ((Scale)shapes.get(i).getTransforms().get(j)).getY() + " " +
                            ((Scale)shapes.get(i).getTransforms().get(j)).getZ() + " ");
                    gotScale = true;
                }

                else if(shapes.get(i).getTransforms().get(j) instanceof Rotate && gotRotate < 2) {
                    writer.print(((Rotate)shapes.get(i).getTransforms().get(j)).getAngle() + " ");
                    gotRotate++;
                }

                else if(shapes.get(i).getTransforms().get(j) instanceof Translate && gotTranslate == false){
                    writer.print(((Translate)shapes.get(i).getTransforms().get(j)).getX() + " " +
                            ((Translate)shapes.get(i).getTransforms().get(j)).getY() + " " +
                            ((Translate)shapes.get(i).getTransforms().get(j)).getZ() + " ");
                    gotTranslate = true;
                }
            }
            writer.println();
        }
        writer.close();
    }

    void loadFile() throws Exception{
        System.out.println("Load");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Shape Files", "*.shp"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));

        ArrayList<String> list = new ArrayList<>();
        String[] subList;
        String st;
        while ((st = reader.readLine()) != null) {
            System.out.println(st);
            list.add(st);
        }

        overallGroup = new Group();

        for(int i = 0; i < list.size(); i++) {
            subList = list.get(i).split(" ");
            for(int j = 0; j < subList.length; j++) {
                System.out.print(subList[j] + "|");
            }
            System.out.println("\n");
            if(list.get(i).contains("Box")) {
                overallGroup.getChildren().add(createBox(subList[2], subList[3], subList[4], subList[5], subList[6], subList[7], subList[8], subList[9], subList[10], subList[11], subList[12], subList[13]));
            }

            else if(list.get(i).contains("Sphere")) {
                overallGroup.getChildren().add(createSphere(subList[2], subList[3], subList[4], subList[5], subList[6], subList[7], subList[8], subList[9], subList[10], subList[11]));
            }

            else if (list.get(i).contains("Cylinder")){
                overallGroup.getChildren().add(createCylinder(subList[2], subList[3], subList[4], subList[5], subList[6], subList[7], subList[8], subList[9], subList[10], subList[11], subList[12]));
            }
            else {
                shapesSubScene.setFill(getColor(subList[0]));
            }
        }
        reader.close();
        shapesGroup.getChildren().setAll(overallGroup.getChildren());
    }

    Box createBox(String width, String height, String depth, String color, String scaleX, String scaleY, String scaleZ,
                  String rotateX, String rotateY, String translateX, String translateY, String translateZ) {
        Box box = new Box(Double.parseDouble(width), Double.parseDouble(height), Double.parseDouble(depth));
        box.setMaterial(new PhongMaterial(getColor(color)));
        box.getTransforms().setAll(new Scale(Double.parseDouble(scaleX), Double.parseDouble(scaleY), Double.parseDouble(scaleZ)),
                new Rotate(Double.parseDouble(rotateX), Rotate.X_AXIS),
                new Rotate(Double.parseDouble(rotateY), Rotate.Y_AXIS),
                new Translate(Double.parseDouble(translateX), Double.parseDouble(translateY), Double.parseDouble(translateZ)));

        box.setOnMouseClicked(event->{
            clickedShape = box;
            material = (PhongMaterial)box.getMaterial();
            this.rotateX = (Rotate)box.getTransforms().get(1);
            this.rotateY = (Rotate)box.getTransforms().get(2);
            scale = (Scale)box.getTransforms().get(0);
            translate = (Translate)box.getTransforms().get(3);

        });
        return box;
    }

    Sphere createSphere(String radius, String color, String scaleX, String scaleY, String scaleZ,
                        String rotateX, String rotateY, String translateX, String translateY, String translateZ) {
        Sphere sphere = new Sphere(Double.parseDouble(radius));
        sphere.setMaterial(new PhongMaterial(getColor(color)));
        sphere.getTransforms().setAll(new Scale(Double.parseDouble(scaleX), Double.parseDouble(scaleY), Double.parseDouble(scaleZ)),
                new Rotate(Double.parseDouble(rotateX), Rotate.X_AXIS),
                new Rotate(Double.parseDouble(rotateY), Rotate.Y_AXIS),
                new Translate(Double.parseDouble(translateX), Double.parseDouble(translateY), Double.parseDouble(translateZ)));

        sphere.setOnMouseClicked(event->{
            clickedShape = sphere;
            material = (PhongMaterial)sphere.getMaterial();
            this.rotateX = (Rotate)sphere.getTransforms().get(1);
            this.rotateY = (Rotate)sphere.getTransforms().get(2);
            scale = (Scale)sphere.getTransforms().get(0);
            translate = (Translate)sphere.getTransforms().get(3);
            //box.getTransforms().addAll(scale,translate);

        });
        return sphere;
    }

    Cylinder createCylinder(String height, String radius, String color, String scaleX, String scaleY, String scaleZ,
                            String rotateX, String rotateY, String translateX, String translateY, String translateZ) {
        Cylinder cyl = new Cylinder(Double.parseDouble(height), Double.parseDouble(radius));
        cyl.setMaterial(new PhongMaterial(getColor(color)));
        cyl.getTransforms().setAll(new Scale(Double.parseDouble(scaleX), Double.parseDouble(scaleY), Double.parseDouble(scaleZ)),
                new Rotate(Double.parseDouble(rotateX), Rotate.X_AXIS),
                new Rotate(Double.parseDouble(rotateY), Rotate.Y_AXIS),
                new Translate(Double.parseDouble(translateX), Double.parseDouble(translateY), Double.parseDouble(translateZ)));


        cyl.setOnMouseClicked(event->{
            clickedShape = cyl;
            material = (PhongMaterial)cyl.getMaterial();
            this.rotateX = (Rotate)cyl.getTransforms().get(1);
            this.rotateY = (Rotate)cyl.getTransforms().get(2);
            scale = (Scale)cyl.getTransforms().get(0);
            translate = (Translate)cyl.getTransforms().get(3);
            //box.getTransforms().addAll(scale,translate);

        });
        return cyl;
    }

    Color getColor(String color) {
        if(color.contains("ff0000")) {
            return Color.RED;
        }
        else if (color.contains("0000ff")) {
            return Color.BLUE;
        }
        else if (color.contains("008000")) {
            return Color.GREEN;
        }
        else if (color.contains("f0ffff")) {
            return Color.AZURE;
        }
        else{
            return Color.WHITE;
        }
    }
}