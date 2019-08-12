package com.hpmtutorial.hpmbooksapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.hpmtutorial.hpmbooksapp.BR;
import com.hpmtutorial.hpmbooksapp.R;
import com.hpmtutorial.hpmbooksapp.databinding.ActivityBooksBinding;
import com.hpmtutorial.hpmbooksapp.model.Book;
import com.hpmtutorial.hpmbooksapp.BR;

import java.util.List;

public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.MyViewHolder> {

    private List<Book> books;
    private OnItemClickListener listener;



    public BooksRecyclerViewAdapter(List<Book> books, OnItemClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.books_recyclerview_item,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(books.get(position), listener);
    }


    @Override
    public int getItemCount() {
        if (books == null) return 0;
        else return books.size();
    }

    public String getBookId(int position) {
        return books.get(position).getId();
    }

    public void setBooksList(List<Book> newBooks){
        this.books = newBooks;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView removeIcon;
        private ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding  binding) {
            super(binding.getRoot());

            this.binding=binding;

            removeIcon = itemView.findViewById(R.id.book_remove_button);

            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRemoveClick(view, getAdapterPosition());
                }
            });
        }

        public void bind(final Book item, final OnItemClickListener listener) {
            this.binding.setVariable(BR.book, item);
            this.binding.executePendingBindings();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Book item);
        void onRemoveClick(View view, int adapterPosition);
    }
}