# Converting JavaFX Layout from Java Code to FXML (XML)

## Overview
FXML (FX Markup Language) is an XML-based UI markup language for JavaFX that allows you to define UI layouts declaratively instead of writing procedural Java code. Your project already has `javafx-fxml` dependency configured!

## Why Convert to FXML?

1. **Cleaner Separation** - UI layout separated from business logic
2. **Easier to Maintain** - Changes to UI don't require code recompilation
3. **Better for Designers** - Visual mockup of layout is clear
4. **Smaller Java Files** - Less boilerplate code in controllers
5. **SceneBuilder Compatible** - Can use visual tools to design UI

---

## Basic Conversion Guide

### 1. Java Layout Code vs FXML

#### Java Code (Current Approach):
```java
HBox hbRoot = new HBox(10);
hbRoot.setAlignment(Pos.CENTER);
hbRoot.setStyle("-fx-padding: 15px;");

Label laPageTitle = new Label("HappyShop");
laPageTitle.setStyle(UIStyle.labelTitleStyle);

TextField tfSearchBar = new TextField();
tfSearchBar.setPromptText("Search for an item by name or ID");
tfSearchBar.setPrefWidth(COLUMN_WIDTH);

hbRoot.getChildren().add(laPageTitle);
hbRoot.getChildren().add(tfSearchBar);
```

#### FXML Equivalent:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<HBox alignment="CENTER" spacing="10" style="-fx-padding: 15px;" 
      xmlns:fx="http://javafx.com/fxml/1">
    <children>
        <Label text="HappyShop" style="-fx-font-size: 24; -fx-font-weight: bold;"/>
        <TextField promptText="Search for an item by name or ID" prefWidth="400"/>
    </children>
</HBox>
```

---

## Common Java-to-FXML Conversions

### Layout Containers

| Java | FXML |
|------|------|
| `new VBox(spacing)` | `<VBox spacing="10">` |
| `new HBox(spacing)` | `<HBox spacing="10">` |
| `new AnchorPane()` | `<AnchorPane>` |
| `new GridPane()` | `<GridPane hgap="10" vgap="10">` |
| `new BorderPane()` | `<BorderPane>` |
| `new StackPane()` | `<StackPane>` |

### Control Properties

| Java | FXML |
|------|------|
| `.setPrefWidth(400)` | `prefWidth="400"` |
| `.setPrefHeight(300)` | `prefHeight="300"` |
| `.setAlignment(Pos.CENTER)` | `alignment="CENTER"` |
| `.setStyle("-fx-padding: 15px;")` | `style="-fx-padding: 15px;"` |
| `.setWrapText(true)` | `wrapText="true"` |
| `.setEditable(false)` | `editable="false"` |
| `.setText("Hello")` | `text="Hello"` or text="#{controller.text}"` |

### Event Handlers

| Java | FXML |
|------|------|
| `button.setOnAction(this::buttonClicked)` | `onAction="#buttonClicked"` |
| `button.setOnMouseClicked(...)` | `onMouseClicked="#handleMouseClick"` |

---

## FXML File Structure

### Header
```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.image.*?>
<?import javafx.scene.shape.*?>
<?import javafx.geometry.Insets?>
```

### Root Element
```xml
<VBox xmlns:fx="http://javafx.com/fxml/1" 
      fx:controller="ci553.happyshop.client.customer.CustomerController"
      spacing="15" 
      alignment="TOP_CENTER">
```

### Children
```xml
    <children>
        <!-- Your nested controls here -->
    </children>
</VBox>
```

---

## Controller Integration

### 1. Linking FXML to Controller
```xml
<VBox xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="ci553.happyshop.client.customer.CustomerController">
```

### 2. Inject FXML Elements into Controller
Use `@FXML` annotation in your controller:
```java
@FXML
private TextField tfSearchBar;

@FXML
private Label lbProductInfo;

@FXML
private ListView<Product> lvCardContainer;
```

### 3. Event Handler in Controller
```java
@FXML
private void buttonClicked(ActionEvent event) {
    // Handler code
}
```

### 4. Loading FXML in Java
```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/customer_view.fxml"));
Parent root = loader.load();
CustomerController controller = loader.getController();
Scene scene = new Scene(root, WIDTH, HEIGHT);
stage.setScene(scene);
```

---

## Advanced Features

### 1. Resource Bundles (for i18n)
```xml
<Label fx:id="label1" text="%label.title"/>
```

### 2. Custom Controls
```xml
<fx:include source="ProductCell.fxml" fx:id="productCell"/>
```

### 3. Style Classes
```xml
<Label text="Title" styleClass="title-label"/>
```

### 4. Data Binding
```xml
<TextField text="${controller.searchText}"/>
```

### 5. Conditional Logic with fx:define
```xml
<fx:define>
    <java.lang.Double fx:id="columnWidth">400.0</java.lang.Double>
</fx:define>
<TextField prefWidth="${columnWidth}"/>
```

---

## Your CustomerView Conversion Strategy

Your `CustomerView.java` has three main pages:
1. **Search Page** - Product listing and search
2. **Trolley Page** - Shopping cart display
3. **Receipt Page** - Order confirmation

### Recommended File Structure:
```
src/
  layout/
    CustomerView.fxml          (Main layout - SearchPage + divider + TrolleyPage)
    SearchPage.fxml            (Left side - search and product listing)
    TrolleyPage.fxml           (Right side - cart display)
    ReceiptPage.fxml           (Right side - receipt display)
    ProductCell.fxml           (Individual product in ListView)
  main/java/
    ci553/happyshop/
      client/customer/
        CustomerView.java      (Main window setup, loads FXML)
        CustomerController.java (Handles FXML events)
```

---

## Step-by-Step Migration

### Phase 1: Create FXML Files
- Convert `createSearchPage()` → `SearchPage.fxml`
- Convert `CreateTrolleyPage()` → `TrolleyPage.fxml`
- Convert `createReceiptPage()` → `ReceiptPage.fxml`

### Phase 2: Update Controller
- Replace Java layout code with FXML loading
- Add `@FXML` annotations for all UI elements
- Keep event handlers in controller

### Phase 3: Test & Refine
- Verify all buttons and controls work
- Test event handlers
- Adjust styling

---

## Tools to Help

1. **SceneBuilder** - Free WYSIWYG tool to design FXML visually
   - Download from: https://gluonhq.com/products/scene-builder/
   - Right-click FXML file → "Edit with SceneBuilder"

2. **Eclipse Plugins** - FXML support and validation
   - Install via Eclipse Marketplace

3. **IntelliJ IDEA** - Built-in FXML support and preview

---

## Common Issues & Solutions

| Problem | Solution |
|---------|----------|
| `FXMLLoadingException` | Check XML syntax, imports, and file path |
| `NullPointerException` on @FXML fields | Use `fx:id` matching variable name |
| Style not applied | Check CSS syntax, use quotes for strings |
| Event not firing | Verify method name matches `onAction="#methodName"` |
| ImageView not loading | Use absolute path or check resource location |

---

## Next Steps

1. Create `CustomerController.java` as the FXML controller
2. Create the FXML files for each page
3. Update `CustomerView.java` to load FXML instead of building UI in Java
4. Gradually migrate one page at a time to minimize errors
