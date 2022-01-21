package com.example.zadanie9;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zadanie9.Book.Book;

import com.example.zadanie9.Book.BookContainer;
import com.example.zadanie9.Book.BookService;
import com.example.zadanie9.Book.RetrofitInstance;
import com.example.zadanie9.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static List<Book> books;



    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                fetchBooksData("");
                return true;
            default:
                return false;
        }
    }
    private void fetchBooksData (String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);
        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {

            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                //Snackbar.make(findViewById(R.id.main_layout), "bruh",Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private String prepareQuery (String query) {
        String []queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }
    private void setupBookListView(List<Book> books) {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter(books);
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class BookHolder extends RecyclerView.ViewHolder {

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private ImageView bookCover;
        Book book;

        public BookHolder (LayoutInflater inflater, ViewGroup parent) {
            super (inflater.inflate(R.layout.book_list_item, parent, false));
            bookTitleTextView = itemView.findViewById (R.id.book_title);
            bookAuthorTextView = itemView.findViewById (R.id.book_autor);
            bookCover = itemView.findViewById(R.id.img_cover);

            itemView.findViewById(R.id.img_cover).setOnClickListener(new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    Intent intent = new Intent(MainActivity.this, activity_details_book.class);

                    Book book;
                    if (MainActivity.books.size() != 0) {
                        book = MainActivity.books.get(0);
                    } else {
                        book = new Book();
                    }
                    for (int i = 0; i < MainActivity.books.size(); i++) {
                        book = MainActivity.books.get(i);
                        if (book.getTitle().equalsIgnoreCase(bookTitleTextView.getText().toString())) {
                            break;
                        }
                    }

                    activity_details_book.authors = book.getAuthors();
                    activity_details_book.title = book.getTitle();
                    activity_details_book.drawable = bookCover.getDrawable();

                    startActivity(intent);
                }
            });


        }

        public void bind (Book book) {
            if (book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null) {
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                //Log.e("tutaj", IMAGE_URL_BASE+book.getCover()+"-S.jpg");
                MainActivity.books.add(book);
                if (book.getCover() != null) {
                    Picasso.with(itemView.getContext()).load(IMAGE_URL_BASE + book.getCover() + ".jpg").placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
                } else {
                    bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                }
            }
        }
    }
    public boolean checkNullOrEmpty(String title) {
        return title != null && !TextUtils.isEmpty(title);
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> books;
        public BookAdapter (List<Book> books) {
            this.books = books;
        }
        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            return new BookHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            } else {
                Log.d("MainActivity", "No books");
            }
        }

        @Override
        public int getItemCount() {
            if (books != null) {
                return books.size();
            } else {
                return 0;
            }
        }

        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        LinkedList<Book> books = new LinkedList<Book>();
        this.books = books;

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        BookAdapter adapter = new BookAdapter(books);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

    }



}