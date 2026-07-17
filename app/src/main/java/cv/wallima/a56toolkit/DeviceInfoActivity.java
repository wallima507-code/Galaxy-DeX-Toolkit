package cv.wallima.a56toolkit;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.reflect.Field;
import cv.wallima.a56toolkit.databinding.ActivityDeviceInfoBinding;

public class DeviceInfoActivity extends AppCompatActivity {
    private ActivityDeviceInfoBinding b; private String report;
    @Override protected void onCreate(Bundle s) { super.onCreate(s); b=ActivityDeviceInfoBinding.inflate(getLayoutInflater()); setContentView(b.getRoot()); refresh(); b.btnRefresh.setOnClickListener(v->refresh()); b.btnCopy.setOnClickListener(v->copy()); }
    private void refresh() {
        Intent i=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level=i==null?-1:i.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale=i==null?100:i.getIntExtra(BatteryManager.EXTRA_SCALE,100);
        int temp=i==null?0:i.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
        int voltage=i==null?0:i.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
        int status=i==null?-1:i.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        BatteryManager bm=(BatteryManager)getSystemService(BATTERY_SERVICE);
        long current=bm.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        report="MODELO\n"+Build.MODEL+"\n\nFABRICANTE\n"+Build.MANUFACTURER+"\n\nANDROID\n"+Build.VERSION.RELEASE+" (API "+Build.VERSION.SDK_INT+")\n\nONE UI\n"+oneUi()+"\n\nBUILD\n"+Build.DISPLAY+"\n\nPATCH DE SEGURANÇA\n"+Build.VERSION.SECURITY_PATCH+"\n\nHARDWARE\n"+Build.HARDWARE+"\n\nPRODUTO\n"+Build.PRODUCT+"\n\nBATERIA\n"+(level*100/scale)+"% • "+status(status)+"\nTemperatura: "+(temp/10f)+" °C\nVoltagem: "+voltage+" mV\nCorrente instantânea: "+current+" µA\n\nNota: ciclos e saúde real podem estar protegidos pelo firmware Samsung.";
        b.tvInfo.setText(report);
    }
    private String status(int s){ if(s==BatteryManager.BATTERY_STATUS_CHARGING)return "A carregar"; if(s==BatteryManager.BATTERY_STATUS_FULL)return "Cheia"; if(s==BatteryManager.BATTERY_STATUS_DISCHARGING)return "Em utilização"; return "Desconhecido"; }
    private String oneUi(){ try{Field f=Build.VERSION.class.getField("SEM_PLATFORM_INT");int sem=f.getInt(null);int major=(sem-90000)/10000+1;int minor=((sem-90000)%10000>=5000)?5:0;return major+"."+minor+" (SEM "+sem+")";}catch(Exception e){return "não detetada";} }
    private void copy(){ ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("A56 Toolkit",report)); Toast.makeText(this,"Relatório copiado",Toast.LENGTH_SHORT).show(); }
}
