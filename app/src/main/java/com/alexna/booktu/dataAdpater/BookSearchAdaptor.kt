package com.alexna.booktu.dataAdpater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexna.booktu.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.book_item.view.*


/**
 * Created by js on 2018-02-11.
 */

class BookSearchAdaptor(val book: BookResult): RecyclerView.Adapter<BookSearchAdaptor.ViewHolder>() {
// setOnClickListener 1. 람다 파라미터 추가


    // 이게 맞냐 ---------------------------------------------------------------------
    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
    //-----------------------------------------------------------------------------


    //아이템의 갯수
    override fun getItemCount(): Int {
        return book.items.count()
    }

    // setOnClickListener 4. 리턴 파라미터 추가
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(book.items.get(position))


        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
        }


    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    // setOnClickListener 3. Holder에서 클릭 처리 > 파라미터에 람다식 추가

        val tr1 = "<b>"
        val tr2 = "</b>"

        fun bindItems(data : Items){
            //
            Glide.with(view.context).load(data.image)
                .apply(RequestOptions().override(300, 450))
                .apply(RequestOptions.centerCropTransform())
                .into(view.bookImg)

            var realTitle = data.title.replace(tr1, "").replace(tr2, "")
            itemView.bookTitle.text = realTitle
            itemView.book_author.text = "작가 ${data.author}"
            itemView.show_publisher.text = "출판사 ${data.publisher}"

        }


    }



}