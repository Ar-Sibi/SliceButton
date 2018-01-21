package slice.ashketchup.slicebutton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ashketchup on 21/1/18.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        SlashHolderView slicer = (SlashHolderView)findViewById(R.id.slicer);
        SlashButton slashButton = new SlashButton(this);
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        slashButton.setSource(b);
        slashButton.listener = new SlashButton.SlashListener() {
            @Override
            public void onSlash() {
                Log.d("TAG","ASKDJHJASDBkjABSDJKabskjdnklasndbjaVSDGHbhn");
            }
        };
        slicer.slashGroup.addButton(slashButton);
    }
}
