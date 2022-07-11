package hr.altima;

import java.util.Timer;
import hr.altima.service.CleanAndReportTask;

public class Runner {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new CleanAndReportTask(), 0, 30000);
    }
}
