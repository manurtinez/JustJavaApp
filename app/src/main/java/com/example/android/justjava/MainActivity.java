package com.example.android.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private int quantity = 0;
    private int price = 5;
    private int creamPrice = 1;
    private int chocolatePrice = 2;
    private String orderText = "";
    private String name;

    /**
     * This function is called when order button is clicked
     */
    public String generateOrder(View view) {
        //get cream topping
        CheckBox addCreamCheckbox = (CheckBox) findViewById(R.id.addCreamCheckbox);
        boolean hasCream = addCreamCheckbox.isChecked();

        //get chocolate topping
        CheckBox addChocolateCheckbox = (CheckBox) findViewById(R.id.addChocolateCheckbox);
        boolean hasChocolate = addChocolateCheckbox.isChecked();

        //get customer name
        EditText nameView = (EditText) findViewById(R.id.nameFieldView);
        this.name = nameView.getText().toString();

        displayQuantity(quantity);

        //calculate total price
        int total = calculatePrice(hasCream, hasChocolate);

        String orderText = createOrderSummary(total, name, hasCream, hasChocolate);
        displaySummary(orderText);
        //composeEmail(total, name, hasCream, hasChocolate);

        return orderText;
    }

    /**
     * This function dislays the quantity specified
     */
    private void displayQuantity(int number) {
        TextView quantityView = (TextView) findViewById(R.id.quantityView);
        quantityView.setText("" + number);
    }

    /**
     * Displays total value on screen
     */
    private void displaySummary(String order) {
        TextView summaryView = (TextView) findViewById(R.id.summaryView);
        summaryView.setText(order);
    }

    /**
     * Increments the amount of coffees
     */
    public void increment(View view) {
        if (quantity < 100 ) {
            this.quantity++;
        }
        displayQuantity(quantity);
    }

    /**
     * Decrements the amount of coffees
     */
    public void decrement(View view) {
        if (quantity > 0) {
            this.quantity--;
        }
        displayQuantity(quantity);
    }

    /**
     * Calculate total price
     */
    private int calculatePrice(boolean hasCream, boolean hasChocolate) {
        int basePrice = quantity * price;
        basePrice = hasCream ? basePrice + (creamPrice * this.quantity) : basePrice;
        basePrice = hasChocolate ? basePrice + (chocolatePrice * this.quantity) : basePrice;
        return basePrice;
    }

    /**
     * Creates the text for the summary of the order
     */

    private String createOrderSummary(int price, String name, boolean hasCream, boolean hasChocolate) {
        String formattedPrice = NumberFormat.getCurrencyInstance().format(price);
        String cream = (hasCream ? getString(R.string.yes) : "No");
        String choco = (hasChocolate ? getString(R.string.yes) : "No");
        return getString(R.string.summaryName, name)
                + getString(R.string.quantity, this.quantity) + this.quantity
                + getString(R.string.hasCreamTopping, cream)
                + getString(R.string.hasChocolateTopping, choco)
                + "\nTotal: " + formattedPrice
                + getString(R.string.thankYou);
    }

    /**
     * Composes mail to send order
     */
    public void composeEmail(View view) {
        String emailSubject = "Coffee order from " + name;
        String emailBody = generateOrder(view);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}