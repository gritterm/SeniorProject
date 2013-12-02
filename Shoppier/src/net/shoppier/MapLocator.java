package net.shoppier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MapLocator extends Activity {
	
	ImageView map; 

	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		setTitle("Find Item");
		Bitmap bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.jenisonmeijermap);
		Bitmap bitMapPoint = BitmapFactory.decodeResource(getResources(), R.drawable.red_dot);
		map = (ImageView) findViewById(R.id.mapView);
		map.setImageBitmap(placePin(bitMap, bitMapPoint, 50, 50));
	}

	
    private Bitmap placePin(Bitmap map, Bitmap point, int x_cor, int y_cor) {
        Bitmap bmOverlay = Bitmap.createBitmap(map.getWidth(), map.getHeight(), map.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(map, new Matrix(), null);
        
        Matrix scaleMatrix = new Matrix();
		scaleMatrix .postTranslate(x_cor, x_cor);
        canvas.drawBitmap(point, scaleMatrix, null);
        return bmOverlay;
    }
	
	

}
