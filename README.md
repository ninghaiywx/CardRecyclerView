# CardRecyclerView
一个展示信息的卡片布局
### 效果图
![](card_recyclerview.gif)

### Gradle引入
<pre>compile 'me.ywx.CardRecyclerView:cardrecyclerview:1.0.0'</pre>

### xml引入
```xml
//引入卡片recyclervieew
    <com.example.cardrecyclerview.CardRecyclerView
        android:id="@+id/recyclerview" 
        android:layout_width="250dp"  //卡片显示的宽度
        android:layout_height="250dp"/>  //卡片显示的高度
```
### RecyclerView的item布局(样例)
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:background="#FFF000"
    android:layout_width="250dp"   //最好与recyclerview宽度一样，可以填充满
    android:layout_height="250dp">  //最好与recyclerview高度一样，可以填充满
    
    <TextView
        android:id="@+id/recycler_item"
        android:layout_centerInParent="true"
        android:textSize="30sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</RelativeLayout>
```

### 创建自己的adapter(样例,对应样例item布局)
```java
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<String>list;
    private Context mContext;

    public Adapter(Context context,List<String>list){
        this.list=list;
        mContext=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text=list.get(position);
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text=(TextView)itemView.findViewById(R.id.recycler_item);
        }
    }
}
```
### 基本用法
```java
    CardRecyclerView recyclerView;
    Adapter adapter;
    List<String> list=new ArrayList<>();
    
    adapter=new Adapter(this,list); //创建自己的适配器
    CardLayoutManager manager=new CardLayoutManager(); //使用卡片布局管理器(必须)
    recyclerView.setLayoutManager(manager);  //设置布局管理器
    recyclerView.setAdapter(adapter);        //设置适配器
```

### 设置监听
```java
//设置移除监听
recyclerView.setOnRemoveListener(new OnRemoveListener() {
            //左侧移除回调
            @Override
            public void onLeftRemove() {
                Toast.makeText(MainActivity.this,"左侧删除"+list.get(list.size()-1),Toast.LENGTH_LONG).show();
                //删除最后一个元素(后面的元素在最上面)
                list.remove(list.size()-1);
                //通知适配器数据改变
                adapter.notifyDataSetChanged();
                Log.d("size",list.size()+"");
            }
            
            //右侧移除回调
            @Override
            public void onRightRemove() {
                Toast.makeText(MainActivity.this,"右侧删除"+list.get(list.size()-1),Toast.LENGTH_LONG).show();
                //删除最后一个元素(后面的元素在最上面)
                list.remove(list.size()-1);
                //通知适配器数据改变
                adapter.notifyDataSetChanged();
                Log.d("size",list.size()+"");
            }
        });
```

### 完整代码
```java
public class MainActivity extends AppCompatActivity {
    CardRecyclerView recyclerView;
    Adapter adapter;
    List<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(CardRecyclerView)findViewById(R.id.recyclerview);
        adapter=new Adapter(this,list);
        CardLayoutManager manager=new CardLayoutManager();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        init();

        recyclerView.setOnRemoveListener(new OnRemoveListener() {
            @Override
            public void onLeftRemove() {
                Toast.makeText(MainActivity.this,"左侧删除"+list.get(list.size()-1),Toast.LENGTH_LONG).show();
                Log.d("remove",list.get(list.size()-1));
                list.remove(list.size()-1);
                adapter.notifyDataSetChanged();
                Log.d("size",list.size()+"");
            }

            @Override
            public void onRightRemove() {
                Toast.makeText(MainActivity.this,"右侧删除"+list.get(list.size()-1),Toast.LENGTH_LONG).show();
                Log.d("remove",list.get(list.size()-1));
                list.remove(list.size()-1);
                adapter.notifyDataSetChanged();
                Log.d("size",list.size()+"");
            }
        });
    }

    private void init() {
        for(int i=0;i<20;i++){
            list.add(i+"");
        }
        adapter.notifyDataSetChanged();
    }
}
```
