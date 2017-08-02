package com.wangku.demo.mall;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class ChoiceParameterDialog extends Dialog {
    private Context context;
    private ImageView ivPic;
    private TextView tvPrice;
    private TextView tvQpl;
    private TextView tvTitle;
    private ImageButton ibReduce;
    private ImageButton ibAdd;
    private EditText etCount;
    private View botttom;
    private List<Param.SpecBean> specList;
    private List<Param.SkuBean> skuList;
    private RecyclerView rv;
    private SpecAdapter adapter;
    private HashMap<Integer, List<ParameterEntity>> outMap;
    private boolean allSelected = false;
    private SelectedListener selectedListener;
    private Param.SkuBean selectedSku;
    private TextView tvConfirm;
    private int qpl = 1;
    private double price;
    private double totleprice;
    private int current;
    private boolean hasChanged;
    private RelativeLayout rlCount;

    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public ChoiceParameterDialog(Context context, Param param) {
        super(context, R.style.dialog);
        this.context = context;
        this.skuList = param.getSku();
        this.specList = param.getSpec();
        init();
        setData();
    }

    private void setData() {
        outMap = new HashMap<>();
        for (int i = 0; i < specList.size(); i++) {
            Param.SpecBean specBean = specList.get(i);
            List<ParameterEntity> innerList = new ArrayList();
            for (int j = 0; j < specBean.getSpecValue().size(); j++) {
                ParameterEntity entity = new ParameterEntity(specBean.getSpecValue().get(j));
                entity.selected = false;
                innerList.add(entity);
            }
            outMap.put(i, innerList);
        }
        rv.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SpecAdapter( specList);
        rv.setAdapter(adapter);
            etCount.setText(qpl + "");
            tvQpl.setText(String.format("购买数量（%d件起批）", qpl));
            checkEnable();
    }

    private void setPrice(String priceString) {
        SpannableString spannableString = new SpannableString(priceString);
        AbsoluteSizeSpan sizeSpan01 = new AbsoluteSizeSpan(12, true);
        AbsoluteSizeSpan sizeSpan02 = new AbsoluteSizeSpan(15, true);
        AbsoluteSizeSpan sizeSpan03 = new AbsoluteSizeSpan(12, true);
        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, priceString.length() - 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan03, priceString.length() - 3, priceString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvPrice.setText("");
        tvPrice.append(spannableString);
    }

    private void init() {
        final View view = View.inflate(context, R.layout.dialog_choice_parameter, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hiddenKeyboard(context, view);
                checkEnable();
                return false;
            }
        });
        rlCount = (RelativeLayout) view.findViewById(R.id.rl_count);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (allSelected) {
                    if (checkEnable()) {
                        if (selectedListener != null) {
                            selectedListener.onComfirm(Integer.parseInt(etCount.getText().toString()), selectedSku, price);
                            dismiss();
                        }
                    }
                }
            }
        });
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hiddenKeyboard(context, view);
                checkEnable();
                return false;
            }
        });
        ivPic = (ImageView) view.findViewById(R.id.iv_pic);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvQpl = (TextView) view.findViewById(R.id.tv_qpl);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ibReduce = (ImageButton) view.findViewById(R.id.ib_reduce);
        ibAdd = (ImageButton) view.findViewById(R.id.ib_add);
        CountChengeClick countChengeClick = new CountChengeClick();
        ibReduce.setOnClickListener(countChengeClick);
        ibAdd.setOnClickListener(countChengeClick);
        etCount = (EditText) view.findViewById(R.id.et_count);

        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        current = Integer.parseInt(s.toString());
                        ibReduce.setEnabled(true);
                        ibAdd.setEnabled(true);
                        if (current <= 1) {
                            ibReduce.setEnabled(false);
                            if (!hasChanged) {
                                hasChanged = true;
                                etCount.setText("1");
                                hasChanged = false;
                            }
                        }
                        if (current >= selectedSku.getInventoryCount()) {
                            current = selectedSku.getInventoryCount();
                            ibAdd.setEnabled(false);
                        }
                        if (!hasChanged) {
                            hasChanged = true;
                            etCount.setText(String.valueOf(current));
                            hasChanged = false;
                        }
                        etCount.setSelection(etCount.getText().toString().length());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        botttom = view.findViewById(R.id.view);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        setContentView(view);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = Utils.getScreenWidth(context);
        getWindow().setAttributes(attributes);

    }

    class CountChengeClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                if (TextUtils.isEmpty(etCount.getText().toString())) {
                    return;
                }
                current = Integer.parseInt(etCount.getText().toString());
                switch (v.getId()) {
                    case R.id.ib_reduce:
                        current--;
                        break;
                    case R.id.ib_add:
                        current++;
                        break;
                }
                checkEnable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    class SpecAdapter extends RecyclerView.Adapter<SpecAdapter.ViewHolder> {
        private List<Param.SpecBean> data;

        public SpecAdapter(List<Param.SpecBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_param_choice, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Param.SpecBean specBean = data.get(position);
            holder.tvTile.setText(specBean.getSpecName());
            holder.flContainer.setHorizontalSpacing(30);
            holder.flContainer.setVerticalSpacing(20);
            ArrayList<ParameterEntity> list = new ArrayList<>();
            for (int i = 0; i < specBean.getSpecValue().size(); i++) {
                ParameterEntity entity = new ParameterEntity(specBean.getSpecValue().get(i));
                entity.enable = computEnable(position, entity.name);
                entity.selected = outMap.get(position).get(i).selected;
                list.add(entity);
            }
            AttrAdapter attrAdapter = new AttrAdapter(position, list);
            holder.flContainer.setAdapter(attrAdapter);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTile;
            FlowLayout flContainer;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTile = (TextView) itemView.findViewById(R.id.tv_title);
                flContainer = (FlowLayout) itemView.findViewById(R.id.fl_container);
            }
        }
    }

    /**
     * 计算是否可以点击
     *
     * @return
     */
    private boolean computEnable(int position, String spacValue) {
        boolean result = false;
        HashMap<Integer, String> selectedMap = new HashMap<>();//已选的属性，key为属性序列，value为属性值
        Iterator<Map.Entry<Integer, List<ParameterEntity>>> entries = outMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, List<ParameterEntity>> entry = entries.next();
            List<ParameterEntity> value = entry.getValue();
            String selected = null;
            for (int i = 0; i < value.size(); i++) {
                if (value.get(i).selected) {
                    selected = value.get(i).name;
                }
            }
            if (selected != null && entry.getKey() != position) {//后一个条件使选中的属性的兄弟属性得以选择
                selectedMap.put(entry.getKey(), selected);
            }
        }
        ArrayList<Param.SkuBean> matchedSku = new ArrayList<>();//筛选出符合 选中要求的sku
        for (int i = 0; i < skuList.size(); i++) {
            boolean matche = true;
            Param.SkuBean sku = skuList.get(i);
            Iterator<Map.Entry<Integer, String>> e = selectedMap.entrySet().iterator();
            while (e.hasNext()) {
                Map.Entry<Integer, String> next = e.next();
                if (!sku.getSpec().get(next.getKey()).equals(next.getValue())) {
                    matche = false;
                }
            }
            if (matche) {
                matchedSku.add(sku);
            }
        }
        //遍历符合要求的sku，如果sku中有该选项，且库存不为零，则可选
        for (int i = 0; i < matchedSku.size(); i++) {
            Param.SkuBean sku = matchedSku.get(i);
            if (sku.getSpec().get(position).equals(spacValue) && sku.getInventoryCount() >= qpl) {
                result = true;
            }
        }
        return result;
    }

    class AttrAdapter extends BaseAdapter {
        private int outPosition;
        private List<ParameterEntity> list;
        public AttrAdapter( int position, List<ParameterEntity> list) {
            this.outPosition = position;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.isEmpty() ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final Holder holder;
            if (vi == null) {
                vi = LayoutInflater.from(context).inflate(R.layout.item_choice_button, null);
                holder = new Holder(vi);
                vi.setTag(holder);
            } else {
                holder = (Holder) vi.getTag();
            }
            ParameterEntity param = list.get(position);
            holder.tv.setText(param.name);
            holder.tv.setEnabled(param.enable);
            holder.tv.setSelected(param.selected);
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!v.isEnabled()) return;
                    List<ParameterEntity> innerList = outMap.get(outPosition);
                    for (int i = 0; i < innerList.size(); i++) {
                        innerList.get(i).selected = false;
                    }
                    innerList.get(position).selected = !holder.tv.isSelected();
                    adapter.notifyDataSetChanged();
                    checkAllChecked();
                }
            });
            return vi;
        }
    }

    private void checkAllChecked() {
        int selectedCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<Integer, List<ParameterEntity>>> entries = outMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, List<ParameterEntity>> entry = entries.next();
            List<ParameterEntity> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                if (value.get(i).selected) {
                    selectedCount++;
                    stringBuilder.append(value.get(i).name + " ");
                }
            }
        }
        String paramString = stringBuilder.toString();
        if (selectedCount == specList.size()) {
            allSelected = true;
            String[] split = paramString.trim().split(" ");
            for (int i = 0; i < skuList.size(); i++) {
                for (int j = 0; j < skuList.get(i).getSpec().size(); j++) {
                    ArrayList<String> strings = new ArrayList<>(skuList.get(i).getSpec());
                    strings.removeAll(Arrays.asList(split));
                    if (strings.size() == 0) {
                        selectedSku = skuList.get(i);
                        break;
                    }
                }
            }
            checkEnable();
            if (selectedListener != null) {
                selectedListener.onSlectedChanged(true, paramString);
            }
        } else {
            allSelected = false;
            selectedSku = null;
            ibAdd.setEnabled(false);
            ibReduce.setEnabled(false);
            etCount.setEnabled(false);
            if (selectedListener != null) {
                selectedListener.onSlectedChanged(false, null);
            }
        }
    }

    private boolean checkEnable() {
        boolean enable = true;
        if (!allSelected) {
            ibAdd.setEnabled(false);
            ibReduce.setEnabled(false);
            etCount.setEnabled(false);
        } else {
            ibAdd.setEnabled(true);
            ibReduce.setEnabled(true);
            etCount.setEnabled(true);
            if (current <= qpl) {
                current = qpl;
                ibReduce.setEnabled(false);
                if (current < qpl) {
                    enable = false;
                }
            }
             if (current >= selectedSku.getInventoryCount()) {
                current = selectedSku.getInventoryCount();
                ibAdd.setEnabled(false);
                if (current > selectedSku.getInventoryCount()) {
                    enable = false;
                }
            }
            etCount.setText(String.valueOf(current));
        }
        return enable;
    }

    class Holder {
        TextView tv;

        public Holder(View view) {
            tv=view.findViewById(R.id.tv);
        }
    }

    public interface SelectedListener {
        void onSlectedChanged(boolean allSelected, String param);

        void onComfirm(int count, Param.SkuBean selectedSku, double price);
    }
}
