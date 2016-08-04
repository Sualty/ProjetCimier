import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

public class Blocks extends QWidget
{

    public Blocks()
    {
        QPushButton quit = new QPushButton(tr("Quit"));
        quit.setFont(new QFont("Times", 18, QFont.Weight.Bold.value()));

        QLCDNumber lcd = new QLCDNumber(2);
        lcd.setSegmentStyle(QLCDNumber.SegmentStyle.Filled);



        QSlider slider = new QSlider(Qt.Orientation.Horizontal);

        slider.setRange(0, 99);

        slider.setValue(0);


        quit.clicked.connect(QApplication.instance(), "quit()");

        slider.valueChanged.connect(lcd, "display(int)");

        QVBoxLayout layout = new QVBoxLayout();

        layout.addWidget(quit);
        layout.addWidget(lcd);
        layout.addWidget(slider);
        setLayout(layout);

        setWindowTitle(tr("Building Blocks"));
    }

    public static void main(String args[])
    {
        QApplication.initialize(args);

        Blocks widget = new Blocks();
        widget.show();

        QApplication.execStatic();
    }
}
