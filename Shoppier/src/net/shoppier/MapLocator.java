package net.shoppier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MapLocator extends Activity {
	
	ImageView map; 
	Paint mpaint; 

	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		setTitle("Find Item");
		Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.test_map);
		
		map = (ImageView) findViewById(R.id.mapView);
		map.setImageBitmap(bitMap);
		mpaint = new Paint(Color.RED);
		Bitmap mutableBitmap = bitMap.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBitmap);
		canvas.drawLine(10, 20, 30, 40, mpaint);
		BitmapDrawable bitMapDraw = new BitmapDrawable(getResources(), mutableBitmap); 
		bitMapDraw.draw(canvas);
	}
	
	
	
	class DrawView extends View {
        Paint paint = new Paint();

        public DrawView(Context context) {
            super(context);
            paint.setColor(Color.BLUE);
        }
        @Override
        public void onDraw(Canvas canvas) {
             super.onDraw(canvas);
                canvas.drawLine(10, 20, 30, 40, paint);
                

        }
}

}
