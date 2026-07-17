package cv.wallima.a56toolkit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ActivityExplorerActivity extends BaseListActivity {
    @Override protected void build() {
        header("Activity Explorer", "Lista atividades exportadas e acessíveis dos pacotes Samsung consultáveis pelo Android.");
        List<String> packages=Arrays.asList("com.android.settings","com.samsung.android.smartmirroring","com.samsung.android.app.settings.bixby");
        List<ActivityInfo> found=new ArrayList<>();
        PackageManager pm=getPackageManager();
        for(String pkg:packages){ try{ PackageInfo pi; if(Build.VERSION.SDK_INT>=33) pi=pm.getPackageInfo(pkg,PackageManager.PackageInfoFlags.of(PackageManager.GET_ACTIVITIES)); else pi=pm.getPackageInfo(pkg,PackageManager.GET_ACTIVITIES); if(pi.activities!=null) for(ActivityInfo ai:pi.activities) if(ai.exported) found.add(ai); }catch(Exception ignored){} }
        found.sort(Comparator.comparing(a->a.name));
        int limit=Math.min(found.size(),120);
        for(int n=0;n<limit;n++){ ActivityInfo ai=found.get(n); String shortName=ai.name.substring(ai.name.lastIndexOf('.')+1); action(shortName+"\n"+ai.packageName,()->open(ai)); }
        if(found.isEmpty()) action("Nenhuma atividade visível\nO Android 16 pode limitar a consulta de pacotes.",()->{});
    }
    private void open(ActivityInfo ai){ try{startActivity(new Intent().setClassName(ai.packageName,ai.name));}catch(Exception e){Toast.makeText(this,"A atividade existe, mas o sistema bloqueou a abertura",Toast.LENGTH_LONG).show();} }
}
