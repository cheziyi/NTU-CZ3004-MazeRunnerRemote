package ntu.cz3004.mazerunnerremote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import ntu.cz3004.mazerunnerremote.R;
import ntu.cz3004.mazerunnerremote.dto.Command;
import ntu.cz3004.mazerunnerremote.engines.BotEngine;
import ntu.cz3004.mazerunnerremote.managers.BluetoothManager;

import static ntu.cz3004.mazerunnerremote.managers.BluetoothManager.bt;

/**
 * Created by Aung on 1/28/2018.
 */

public class CheckC567Fragment extends MainFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener, View.OnTouchListener {

    //Checklist C7 components
    private TextView updateModeTextVuew;
    private Switch updateModeSwitch;
    private Button updateButton;

    //Canvas view
    private BotEngine botEngine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_c567, container, false);
        updateModeTextVuew = view.findViewById(R.id.updateModeTextVuew);
        updateModeSwitch = view.findViewById(R.id.updateModeSwitch);
        updateButton = view.findViewById(R.id.updateButton);
        botEngine = view.findViewById(R.id.botEngine);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateModeSwitch.setOnCheckedChangeListener(this);
        updateButton.setOnClickListener(this);
        botEngine.setOnTouchListener(this);
        botEngine.setOnLongClickListener(this);
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.nav_check_c567;
    }

    @Override
    public void onPause() {
        super.onPause();
        botEngine.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        botEngine.resume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.updateButton:
                BluetoothManager.SendCommand(new Command(Command.CommandTypes.SEND_INFO));
                //botEngine.draw();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()){
            case R.id.updateModeSwitch:
                changeUpdateMode(isChecked);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.botEngine:
                if(botEngine.touchBot()){
                    printLog("long");
                    botEngine.setEditMode(true);
                    return true;
                }
                botEngine.setWaypoint();
                break;

        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.botEngine:
                int[] touchCoor = new int[2];
                touchCoor[0] = (int) motionEvent.getX();
                touchCoor[1] = (int) motionEvent.getY();
                botEngine.setTouchX((int) motionEvent.getX()); //getX() return coordinate RELATIVE to the view dispatched it
                botEngine.setTouchY((int) motionEvent.getY());
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        botEngine.setBotCanvas(touchCoor);
                        return false; //return false so that onLongClick get triggered
                    case MotionEvent.ACTION_MOVE:
                        botEngine.setBotPosition(touchCoor);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(botEngine.isEditMode()){
                            bt.send("coordinate (" + botEngine.getBotX() + "," + botEngine.getBotY() + ")", false);
                            botEngine.setEditMode(false);
                        }
                        return false;
                }
                break;
        }
        return false;
    }

    private void changeUpdateMode(boolean isManualMode) {
        updateButton.setEnabled(isManualMode);
        updateModeTextVuew.setText(isManualMode ? "Update Mode (manual)" : "Update Mode (auto)");
        botEngine.setAutoUpdating(!isManualMode);
    }

    private void printLog(String message){
        Log.v("debugBot", message);
    }
}