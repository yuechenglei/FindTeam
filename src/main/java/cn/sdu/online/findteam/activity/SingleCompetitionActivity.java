package cn.sdu.online.findteam.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sdu.online.findteam.R;
import cn.sdu.online.findteam.adapter.SingleCompetitionListAdapter;
import cn.sdu.online.findteam.entity.SingleCompetitionListItem;

/**
 * 具体的单个比赛信息界面
 */
public class SingleCompetitionActivity extends Activity {

    private ListView singlelistView;

    /**
     * adapter 中要传入的数据
     */
    private ImageView imageView;
    private TextView teamname;
    private TextView personnum;
    private View line1;
    private TextView content;
    private View line2;
    private Button look;
    private Button join;

    private List<SingleCompetitionListItem> listItems;

    private String teamnametext;
    private String personnumtext;
    private String contenttext;

    /**
     * ActionBar上的返回 Button
     */
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarLayout(R.layout.singlecompetition_actionbar_layout);
        setContentView(R.layout.singlecompetition_layout);

        singlelistView = (ListView) findViewById(R.id.singlecplist);
        /*imageView = (ImageView) findViewById(R.id.singlecp_item_img);
        teamname = (TextView) findViewById(R.id.singlecp_item_teamname);
        personnum = (TextView) findViewById(R.id.singlecp_item_personnum);
        line1 = findViewById(R.id.singlecp_item_line1);
        content = (TextView) findViewById(R.id.singlecp_item_content);
        line2 = findViewById(R.id.singlecp_item_line2);
        look = (Button) findViewById(R.id.singlecp_item_look);
        join = (Button) findViewById(R.id.singlecp_item_join);*/

        initListView();

        returnButton = (Button) findViewById(R.id.singlecp_return_bt);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleCompetitionActivity.this.finish();
            }
        });
    }

    public void initListView(){
        contenttext = "“南航E行”团队是一支充满激情、踏实肯干、有着创新精神的团队。这个队名也很朴实，我们赋予了她深深的感情。南航是我们的母校，是授以我们知识的地方，也是莘莘学子寻梦的地方。e行是我们参赛的态度，也是我们做人做事的信念。相信只要我们不放弃，不抛弃，走到最后我们还是e路通行";
        personnumtext = "缺2人";
        teamnametext = "赵亮夜行队";

        listItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            listItems.add(new SingleCompetitionListItem(R.id.singlecp_item_img, teamnametext,
                    personnumtext, R.id.singlecp_item_line1,
                    contenttext, R.id.singlecp_item_line2,
                    R.id.singlecp_item_look, R.id.singlecp_item_join));
        }

        SingleCompetitionListAdapter singlecompetitionListAdapter = new SingleCompetitionListAdapter(SingleCompetitionActivity.this,
                listItems);
        singlelistView.setAdapter(singlecompetitionListAdapter);
    }

    /**
     * @param layoutId 布局Id
     */
    public void setActionBarLayout(int layoutId) {
        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(layoutId, null);
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionMenuView.LayoutParams.FILL_PARENT, ActionMenuView.LayoutParams.FILL_PARENT);
            actionBar.setCustomView(v, layout);
        }
    }


}
