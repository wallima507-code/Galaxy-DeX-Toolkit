package cv.wallima.a56toolkit;

import android.provider.Settings;

public class HiddenSettingsActivity extends BaseListActivity {
    @Override protected void build() {
        header("Definições ocultas", "Atalhos do Android e da Samsung; a disponibilidade depende do firmware.");
        action("Opções de programador", () -> SystemTools.openIntent(this,Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS,"Programador"));
        action("Depuração sem fios", () -> SystemTools.openIntent(this,"android.settings.WIRELESS_DEBUGGING_SETTINGS","Depuração sem fios"));
        action("Aplicações com acesso especial", () -> SystemTools.openIntent(this,"android.settings.MANAGE_SPECIAL_APP_ACCESSES","Acesso especial"));
        action("Otimização da bateria", () -> SystemTools.openIntent(this,Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS,"Bateria"));
        action("NFC", () -> SystemTools.openIntent(this,Settings.ACTION_NFC_SETTINGS,"NFC"));
        action("Painel de privacidade", () -> SystemTools.openIntent(this,"android.settings.PRIVACY_DASHBOARD_SETTINGS","Privacidade"));
        action("Serviços em execução", () -> SystemTools.openFirst(this,"Serviços em execução","com.android.settings","com.android.settings.Settings$RunningServicesActivity"));
        action("Labs / Multi Window", () -> SystemTools.openFirst(this,"Labs","com.android.settings","com.android.settings.Settings$LabsSettingsActivity","com.android.settings.Settings$MultiWindowSettingsActivity"));
    }
}
