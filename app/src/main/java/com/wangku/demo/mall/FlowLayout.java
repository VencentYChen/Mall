package com.wangku.demo.mall;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {
    private int horizontalSpacing = 20;//水平间距
    private int verticalSpacing = 10;//垂直间距
    private AdapterView.OnItemClickListener itemClickListener;
    private DataSetObserver obv;
    private onSizeChangedCallBack callBack;
    private int height = 0;
    private int size = 0;

    private class ItemProxyClickListener implements OnClickListener {
        int pos;

        ItemProxyClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(null, v, pos, 0);
        }
    }

    //用于存放所有的line
    private ArrayList<Line> lineList = new ArrayList<Line>();
    private BaseAdapter adapter;

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context) {
        super(context);
    }

    /**
     * 设置水平间距
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    /**
     * 设置垂直间距
     *
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public void setAdapter(BaseAdapter adp) {
        adapter = adp;
        FlowLayout.this.removeAllViews();
        int total = adapter.getCount();
        for (int i = 0; i < total; i++) {
            View v = adapter.getView(i, null, this);
            if (getItemClickListener() != null) {
                v.setOnClickListener(new ItemProxyClickListener(i));
            }
            addView(v);
        }
        obv = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                removeAllViews();
                int total = adapter.getCount();
                for (int i = 0; i < total; i++) {
                    View v = adapter.getView(i, null, FlowLayout.this);
                    if (getItemClickListener() != null) {
                        v.setOnClickListener(new ItemProxyClickListener(i));
                    }
                    addView(v);
                }
            }
        };
        adapter.registerDataSetObserver(this.obv);
    }

    public void setOnItemClickListener(
            AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public AdapterView.OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        lineList.clear();
        //获取总宽度，是包含paddingLeft和paddingRight
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //获取用于比较的宽度,就是减去左右padding的宽度
        int noPaddingWidth = width - getPaddingLeft() - getPaddingRight();

        //遍历所有的子TextView，根据宽度进行比较和分行
        Line line = null;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //先测量childView，目的是保证能够获取到宽高
            childView.measure(0, 0);//系统发现传的是0,0等非法值，则会按照TextView自己的宽高测量

            if (line == null) {
                line = new Line();//只要不换行，是同一个line，如果换行则是新的line
            }
            //1.如果当前line没有子view，则直接将childView放入line中，不用判断
            //因为要保证每行至少有一个子view
            if (line.getViewList().size() == 0) {
                line.addView(childView);
            } else if (line.getWidth() + childView.getMeasuredWidth() + horizontalSpacing > noPaddingWidth) {
                //2.说明childView换行,先保存当前line，再创建新的line
                lineList.add(line);

                //将childView放入新的line中
                line = new Line();
                line.addView(childView);

            } else {
                //3.说明childView需要加到当前行
                line.addView(childView);
            }

            //如果当前childView是最后一个，则需要将最后的line保存到lineList，否则会造成最后的line丢失
            if (i == (getChildCount() - 1)) {
                lineList.add(line);//将最后的line保存起来
            }
        }

        //for循环结束后，我们有了存放好每行数据的lineList
        //计算FlowLayout需要的高度
        int height = getPaddingTop() + getPaddingBottom();//首先算上paddingTop和paddingBottom
        for (int i = 0; i < lineList.size(); i++) {
            height += lineList.get(i).getHeight();//再算上所有line的高度
        }

        height += (lineList.size() - 1) * verticalSpacing;//再加上所有行的垂直

        setMeasuredDimension(width, height);//向父view申请宽高的

    }

    /**
     * 遍历所有的line，将每个line中的子TextView放置到对应的位置上
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (size == 0) {
            size = lineList.size();
        }
        for (int i = 0; i < lineList.size(); i++) {

            Line line = lineList.get(i);//获取每个line

            if (i > 0) {
                //除去第一行之后的每行的top，都比上一行多一个行高和verticalSpacing
                paddingTop += line.getHeight() + verticalSpacing;
            }

            ArrayList<View> viewList = line.getViewList();//获取每个line中的子view
            //1.计算留白区域
            float remainSpacing = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getWidth();
            //2.计算每个子view可得到的spacing
            float perSpacing = remainSpacing / viewList.size();

            for (int j = 0; j < viewList.size(); j++) {
                View childView = viewList.get(j);//获取每个子view
                //3.将perSpacing分到childView的宽度上面，就是需要重新测量childView
                int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()), MeasureSpec.EXACTLY);
                childView.measure(widthMeasureSpec, 0);//高度传0，系统会按照它本身高度测量
                if (j == 0) {
                    //第一个子view的left是靠左和靠上摆放的
                    childView.layout(paddingLeft, paddingTop
                            , paddingLeft + childView.getMeasuredWidth()
                            , paddingTop + childView.getMeasuredHeight());
                } else {
                    View preChildView = viewList.get(j - 1);//获取前一个子view
                    int left = preChildView.getRight() + horizontalSpacing;//childView的左边
                    childView.layout(left, preChildView.getTop(),
                            left + childView.getMeasuredWidth(),
                            preChildView.getBottom());
                }
            }
        }
    }

    /**
     * 封装每一行的TextView,
     *
     * @author Administrator
     */
    class Line {
        ArrayList<View> viewList;//用于记录当前行所有TextView
        int width;//用于记录当前line的宽，实际是当前所有子view的宽+水平间距
        int height;//其实子view的高度

        public Line() {
            viewList = new ArrayList<View>();
        }

        /**
         * 记录view
         *
         * @param view
         */
        public void addView(View view) {
            //如果不包含才添加
            if (!viewList.contains(view)) {

                //每次addView的时候更新width
                if (viewList.size() == 0) {
                    //第一次添加
                    width = view.getMeasuredWidth();
                } else {
                    width += view.getMeasuredWidth() + horizontalSpacing;
                }
                //给高度赋值,在这里高度都是一样的
                height = Math.max(height, view.getMeasuredHeight());

                viewList.add(view);
            }
        }

        public ArrayList<View> getViewList() {
            return viewList;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public interface onSizeChangedCallBack {
        void onSizeChanged(int height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (callBack != null)
            if (height == 0) {
                callBack.onSizeChanged(h);
                height = h;
            }
    }

    public void setOnSizeChangedCallBack(onSizeChangedCallBack callBack) {
        this.callBack = callBack;
    }

    public int getlineSize() {
        return size;
    }
}
