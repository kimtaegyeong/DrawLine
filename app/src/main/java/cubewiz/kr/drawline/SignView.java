package cubewiz.kr.drawline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by 1002230 on 16. 4. 19..
 */
public class SignView extends View {
    ArrayList<Vertex> arVertex;

    Paint mPaint;       // 페인트 객체 선언

    Bitmap bm;
    Canvas c;

    public SignView(Context context) {
        super(context);
        init();
    }

    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        arVertex = new ArrayList<Vertex>();

    }

    /**
     * 터치이벤트를 받는 함수
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                arVertex.add(new Vertex(event.getX(), event.getY(), false));
                break;
            case MotionEvent.ACTION_MOVE:
                arVertex.add(new Vertex(event.getX(), event.getY(), true));
                break;
            case MotionEvent.ACTION_UP: // 터치때는 순간 Base64 변환
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Log.d("TAG","ByteArray :   "+ byteArray);

                String str = Base64.encodeToString(byteArray, Base64.DEFAULT);

                Log.d("TAG","Base64 : "+ str);
                break;
        }

        invalidate();       // onDraw() 호출
        return true;
    }

    /**
     * 화면을 계속 그려주는 함수
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE); // 캔버스 배경색깔 설정
        mPaint = new Paint();

        bm = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas();
        c.setBitmap(bm);

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(7);
        mPaint.setAntiAlias(true);      // 안티얼라이싱

        // 그리기
        for (int i = 0; i < arVertex.size(); i++) {
            if (arVertex.get(i).draw) {       // 이어서 그리고 있는 중이라면
                c.drawLine(arVertex.get(i - 1).x, arVertex.get(i - 1).y,
                        arVertex.get(i).x, arVertex.get(i).y, mPaint);
                // 이전 좌표에서 다음좌표까지 그린다.
            } else {
                c.drawPoint(arVertex.get(i).x, arVertex.get(i).y, mPaint);
                // 점만 찍는다.
            }
        }

        canvas.drawBitmap(bm, 0, 0, mPaint);

    }

    public class Vertex {
        float x;
        float y;
        boolean draw;   // 그리기 여부

        public Vertex(float x, float y, boolean draw) {
            this.x = x;
            this.y = y;
            this.draw = draw;
        }
    }

}
