package ua.lpnu.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    ViewGroup m_my_list;
    LocalDatabase m_local_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_my_list = (ViewGroup) findViewById(R.id.list);
        m_local_db = new LocalDatabase(getApplicationContext());


        Vector<String> strings = m_local_db.getItems();

        for (String s : strings) {
            addListItem(s);
        }

        findViewById(R.id.detail).setOnClickListener(this);
        ((EditText) findViewById(R.id.edit_text)).setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findViewById(R.id.btn_save).performClick();
                    return true;
                }
                return false;
            }
        });
    }

    void addListItem(String item_name) {
        LayoutInflater l = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = l.inflate(R.layout.list_item, null);

        ((TextView) item.findViewById(R.id.itemtext)).setText(item_name);
        item.findViewById(R.id.morebutton).setOnClickListener(this);

        m_my_list.addView(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.morebutton) {
            View detailview = findViewById(R.id.detail);
            float width = findViewById(R.id.main_layout).getWidth();
            TranslateAnimation anim = new TranslateAnimation(width, 0.0f, 0.0f, 0.0f);
            anim.setDuration(300);
            anim.setFillAfter(true);
            detailview.bringToFront();
            detailview.startAnimation(anim);
            detailview.setVisibility(View.VISIBLE);
            detailview.setEnabled(true);
        } else if (v.getId() == R.id.detail) {
            View detailview = v;
            TranslateAnimation anim = new TranslateAnimation(0.0f, detailview.getWidth(), 0.0f, 0.0f);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    findViewById(R.id.listview).bringToFront();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            anim.setDuration(300);
            anim.setFillAfter(true);
            detailview.startAnimation(anim);
            detailview.setEnabled(false);
        }
    }

    public void onDeleteButtonClick(View button) {
        Vector<View> views_to_delete = new Vector<View>();
        Vector<String> items_to_delete = new Vector<String>();

        for (int i = 0; i < m_my_list.getChildCount(); ++i) {
            View item = m_my_list.getChildAt(i);
            CheckBox cb = item.findViewById(R.id.itemcheck);
            TextView tv = item.findViewById(R.id.itemtext);
            if (cb != null && tv != null) {
                if (cb.isChecked()) {
                    views_to_delete.add(item);
                    items_to_delete.add(tv.getText().toString());
                }
            }
        }

        for (String item_name : items_to_delete) {
            m_local_db.deleteItem(item_name);
        }

        for (View v : views_to_delete) {
            ViewGroup g = (ViewGroup) v.getParent();
            if (g != null) g.removeView(v);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String item = v.getText().toString();
            if (item != null && item.length() > 0) {
                m_local_db.addItem(item);
                addListItem(item);
                ((ScrollView) findViewById(R.id.scroll_view)).requestLayout();
                ((ScrollView) findViewById(R.id.scroll_view)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        }
        return false;
    }

    public void onSaveButtonClick(View button) {
        Editable text = ((EditText) findViewById(R.id.edit_text)).getText();
        String inputText = text.toString();

        if (inputText != null) {
            m_local_db.addItem(inputText);
            addListItem(inputText);

            text.clear();
        }
    }
}
