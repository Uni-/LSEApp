package com.navercorp.android.lseapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.navercorp.android.lseapp.R;
import com.navercorp.android.lseapp.model.DocumentComponentType;

public class DocumentComponentAdderView extends RelativeLayout implements View.OnClickListener {

    private Button mAddTextButton;
    private Button mAddImageButton;
    private Button mAddMapButton;

    private OnComponentAddListener mOnComponentAddListener;

    public DocumentComponentAdderView(Context context) {
        super(context);
        init();
    }

    public DocumentComponentAdderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DocumentComponentAdderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_document_component_adder, this);

        mAddTextButton = (Button) findViewById(R.id.view_document_component_adder_text);
        mAddImageButton = (Button) findViewById(R.id.view_document_component_adder_image);
        mAddMapButton = (Button) findViewById(R.id.view_document_component_adder_map);

        mAddTextButton.setOnClickListener(this);
        mAddImageButton.setOnClickListener(this);
        mAddMapButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_document_component_adder_text: {
                if (mOnComponentAddListener != null) {
                    mOnComponentAddListener.onAddComponent(this, DocumentComponentType.TEXT);
                }
                return;
            }
            case R.id.view_document_component_adder_image: {
                if (mOnComponentAddListener != null) {
                    mOnComponentAddListener.onAddComponent(this, DocumentComponentType.IMAGE);
                }
                return;
            }
            case R.id.view_document_component_adder_map: {
                if (mOnComponentAddListener != null) {
                    mOnComponentAddListener.onAddComponent(this, DocumentComponentType.MAP);
                }
                return;
            }
            default:
        }
    }

    public void setOnComponentAddListener(OnComponentAddListener onComponentAddListener) {
        this.mOnComponentAddListener = onComponentAddListener;
    }

    public interface OnComponentAddListener {
        void onAddComponent(View v, DocumentComponentType componentType);
    }
}
