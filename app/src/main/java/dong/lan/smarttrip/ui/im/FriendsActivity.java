package dong.lan.smarttrip.ui.im;

import android.os.Bundle;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.BaseBarActivity;

public class FriendsActivity extends BaseBarActivity {

    private ContactFragment contactFragment = new ContactFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        bindView("好友");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.friends_container,contactFragment).commit();
    }
}
