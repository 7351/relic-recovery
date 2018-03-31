package org.firstinspires.ftc.teamcode.robotlibrary.pop;

import android.os.Environment;
import android.support.annotation.Nullable;

import com.google.common.io.Files;
import com.kauailabs.navx.ftc.AHRS;
import com.kauailabs.navx.ftc.navXPIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dynamic Signals on 12/29/2016.
 */

public interface Routine {
    void run();
    boolean isCompleted();
    void completed();
}
