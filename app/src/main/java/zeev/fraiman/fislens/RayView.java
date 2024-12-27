package zeev.fraiman.fislens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class RayView extends View {
    private float f, d, h, hPrime, fPrime;
    private float scaleFactor = 1.0f; // Масштабный коэффициент для вписывания в экран

    private Paint axisPaint, objectPaint, focusPaint, ray1Paint, ray2Paint, ray3Paint;

    public RayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Инициализация кисти для оси и линзы
        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(5); // Толщина оси

        // Инициализация кисти для объектов (предмет и изображение)
        objectPaint = new Paint();
        objectPaint.setColor(Color.RED);
        objectPaint.setStrokeWidth(10);

        // Инициализация кисти для фокусов (синие закрашенные кружки)
        focusPaint = new Paint();
        focusPaint.setColor(Color.BLUE);
        focusPaint.setStyle(Paint.Style.FILL); // Закрашенные кружки

        // Инициализация кистей для лучей
        ray1Paint = new Paint();
        ray1Paint.setColor(Color.GREEN);
        ray1Paint.setStrokeWidth(3); // Луч 1 (зеленый)

        ray2Paint = new Paint();
        ray2Paint.setColor(Color.MAGENTA);
        ray2Paint.setStrokeWidth(3); // Луч 2 (пурпурный)

        ray3Paint = new Paint();
        ray3Paint.setColor(Color.CYAN);
        ray3Paint.setStrokeWidth(3); // Луч 3 (голубой)
    }

    public void setData(float f, float d, float h, float hPrime, float fPrime) {
        this.f = f;
        this.d = d;
        this.h = h;
        this.hPrime = hPrime;
        this.fPrime = fPrime; // новое значение f'
        invalidate(); // Перерисовать View
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Задаем отступ от границ View
        float padding = 50; // Отступ в пикселях

        // Получаем размеры View с учетом отступа
        int viewWidth = getWidth() - (int)padding * 2;
        int viewHeight = getHeight() - (int)padding * 2;

        // Центр View для рисования с учетом отступа
        int centerX = (viewWidth / 2) + (int)padding;
        int centerY = (viewHeight / 2) + (int)padding;

        // Рассчитываем масштабный коэффициент с учетом отступа
        float maxDistance = Math.max(d, fPrime);
        scaleFactor = Math.min((viewWidth / (2 * maxDistance)), (viewHeight / (2 * Math.max(h, hPrime))));
        /*// Рассчитываем максимальное расстояние (d + f' или d + f) для масштабирования
        float maxDistance = Math.max(d, fPrime);

        // Рассчитываем масштабный коэффициент, чтобы все элементы вписывались в экран
        scaleFactor = Math.min(viewWidth / (2 * maxDistance), viewHeight / (2 * Math.max(h, hPrime)));*/

        // Умножаем все значения на масштабный коэффициент
        float scaledD = d * scaleFactor;
        float scaledF = f * scaleFactor;
        float scaledFPrime = fPrime * scaleFactor; // масштабируем f'
        float scaledH = h * scaleFactor;
        float scaledHPrime = hPrime * scaleFactor;

        // Рисуем оптическую ось (горизонтальная линия)
        canvas.drawLine(0, centerY, viewWidth, centerY, axisPaint);

        // Рисуем линзу в центре
        canvas.drawLine(centerX, 0, centerX, viewHeight, axisPaint);

        // Рассчитываем координаты объекта и изображения с учетом масштаба
        float objectX = centerX - scaledD; // Объект слева от центра
        float imageX = centerX + scaledFPrime;  // Изображение справа от центра (используем f')
        float objectY = centerY - scaledH;
        float imageY = centerY + scaledHPrime;

        // Рисуем точки объекта и изображения
        canvas.drawCircle(objectX, objectY, 10, objectPaint);
        canvas.drawCircle(imageX, imageY, 10, objectPaint);

        // Рисуем фокусы на оптической оси (синие закрашенные кружки)
        float focusRadius = axisPaint.getStrokeWidth() * 2;

        // Левый фокус (на расстоянии f слева от линзы)
        float leftFocusX = centerX - scaledF;
        canvas.drawCircle(leftFocusX, centerY, focusRadius, focusPaint);

        // Правый фокус (на расстоянии f справа от линзы)
        float rightFocusX = centerX + scaledF;
        canvas.drawCircle(rightFocusX, centerY, focusRadius, focusPaint);

        // Первый луч: от объекта к линзе параллельно оси, затем через правый фокус
        canvas.drawLine(objectX, objectY, centerX, objectY, ray1Paint); // До линзы параллельно оси
        canvas.drawLine(centerX, objectY, imageX, imageY, ray1Paint); // Через правый фокус

        // Второй луч: от объекта через левый фокус, затем параллельно оси
        canvas.drawLine(objectX, objectY, centerX, imageY, ray2Paint); // До линзы через левый фокус
        canvas.drawLine(centerX, imageY, imageX, imageY, ray2Paint); // Параллельно оси к изображению

        // Третий луч: от объекта через центр линзы (оптическая ось)
        canvas.drawLine(objectX, objectY, imageX, imageY, ray3Paint); // Через центр линзы
    }

}
