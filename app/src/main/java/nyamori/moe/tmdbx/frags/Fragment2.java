package nyamori.moe.tmdbx.frags;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nyamori.moe.tmdbx.collection.CollectionList;
import nyamori.moe.tmdbx.obj.CurrentUser;
import nyamori.moe.tmdbx.Login;
import nyamori.moe.tmdbx.MainActivity;
import nyamori.moe.tmdbx.R;
import nyamori.moe.tmdbx.Register;

public class Fragment2 extends Fragment {
    Button login;
    Button register;
    TextView username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment2, container, false);
         login=rootView.findViewById(R.id.login);
         register=rootView.findViewById(R.id.register);
         username=rootView.findViewById(R.id.username);
        if(CurrentUser.getUser().equals("")){
            login.setVisibility(View.VISIBLE);
            register.setVisibility(View.VISIBLE);
            username.setVisibility(View.INVISIBLE);
        }else{
            login.setVisibility(View.INVISIBLE);
            register.setVisibility(View.INVISIBLE);
            username.setVisibility(View.VISIBLE);

            username.setText(CurrentUser.getUser());
        }
        final BottomBar bottomBar = (BottomBar) getActivity().findViewById(R.id.bottomBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getActivity(),Login.class);

                startActivity(intent);

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getActivity(),Register.class);

                startActivity(intent);

            }
        });



        ListView lv=rootView.findViewById(R.id.lv);
        List<Map<String,String>> data=new ArrayList<>();
        String[] info= new String[]{"搜索影视","正在热映","我的收藏","退出登录"};
        for(int i=0;i<4;i++){
            Map<String,String>temp = new LinkedHashMap<>();
            temp.put("MSG",info[i]);
            data.add(temp);
        }


        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),data,R.layout.information,new String[] {"MSG"},new int[]{R.id.message});
        lv.setAdapter(simpleAdapter);

        lv.setOnItemClickListener(new  AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 处理单击事件
                if(CurrentUser.getUser().equals("")&&(i==2||i==3)){
                    Toast.makeText(getActivity(), "请先登录.", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (i){
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment1()).commit();
                        bottomBar.selectTabAtPosition(0);
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fragment3()).commit();
                        bottomBar.selectTabAtPosition(1);
                        break;
                    case 2:
                        Intent intent = new Intent(getActivity(), CollectionList.class);
                        startActivity(intent);
                        break;
                    case 3:
                        CurrentUser.setUser("");
                        login.setVisibility(View.VISIBLE);
                        register.setVisibility(View.VISIBLE);
                        username.setVisibility(View.INVISIBLE);

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle("退出");
                        alertDialog.setMessage("确定退出登录？");
                        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                CurrentUser.setUser("");
                                login.setVisibility(View.VISIBLE);
                                register.setVisibility(View.VISIBLE);
                                username.setVisibility(View.INVISIBLE);
                                Intent intent= new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);

                            }
                        }).show();
                        break;
                    default:
                        break;
                }




            }
        } );





        return rootView;
    }

}
