package ru.ugasu.app.service.build;

import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

public class LoggedBuildResponseItem extends BuildImageResultCallback {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggedBuildResponseItem.class.getSimpleName());

    private PrintStream printStream;

    public LoggedBuildResponseItem(PrintStream printStream) {
        this.printStream = printStream;
    }

    public LoggedBuildResponseItem() {
        super();
    }

    @Override
    public void onNext(BuildResponseItem item) {
        try {
            String stream = item.getStream();
            if (stream != null) {
                stream = stream.trim();
                if (!stream.isEmpty() && !stream.startsWith("--->")) {
                    printStream.println(stream);
                }
            }
        } catch (Exception e) {
            LOGGER.info("Failed to save results. {}. {}", e.getMessage(), e.getCause());
        }
        super.onNext(item);
    }


}
