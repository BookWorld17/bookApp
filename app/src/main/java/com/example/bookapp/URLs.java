package com.example.bookapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.bookapp.models.Category;

import java.util.ArrayList;

public  class URLs {

    public static final String BASE = "http://192.168.1.11/2022/book_world/";
    public static final String SERVER = BASE+"apis/";
    public static final String login = SERVER + "login.php";
    public static final String signup = SERVER + "singup.php";
    public static final String ADD_BOOK = SERVER + "addBook.php";
    public static final String GET_BOOKS = SERVER + "listBooks.php";
    public static final String GET_MY_BOOKS = SERVER + "listMyBook.php";
    public static final String ADD_COPY_BOOK = SERVER + "addCopyBook.php";
    public static final String GET_COPIES_BOOK = SERVER + "getListOfCopiesForBook.php";
    public static final String DELETE_BOOK = SERVER + "deleteBook.php";
    public static final String DELETE_BOOKCOPY = SERVER + "deleteCopyBook.php";
    public static final String SET_ORDER = SERVER + "setOrder.php";
    public static final String GET_ORDERS = SERVER + "getListOrders.php" ;
    public static final String GET_CATEGORIES = SERVER + "getCategories.php" ;
    public static final String OPEN_DISPUTE = SERVER + "openDispute.php" ;;
    public static final String GET_DISPUTS = SERVER + "getListDiputes.php" ;
    public static final String ADD_TO_CART = SERVER + "addToCart.php" ;
    public static final String GET_CART =  SERVER + "getCart.php" ;
    public static final String CART_REMOVE_ITEM = SERVER + "cartRemoveItem.php" ;
    public static final String CONFIRM_ORDER = SERVER + "confirmOrder.php" ;
    public static final String GET_ORDER_DETAILS = SERVER + "getOrderDetails.php" ;
    public static final String CHAT = BASE + "chat/chat.php";
    public static final String ADMINCHAT = BASE + "adminchat/chat.php";
    public static final String SET_REVIEW = SERVER + "set_review.php" ;
    public static final String GET_REVIEWS = SERVER + "get_reviews.php" ;
    public static ArrayList<Category> categoriesList = new ArrayList<>();


    public static void sendEmail(Activity context, String buyerEmail){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ buyerEmail});
        email.putExtra(Intent.EXTRA_SUBJECT, "Order Confirmation");
        email.putExtra(Intent.EXTRA_TEXT, "Your order is received.");

//need this to prompts email client only
        email.setType("message/rfc822");

        context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

}
