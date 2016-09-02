package com.b_designworks.android.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class EditProfileScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_edit_profile)
            .enableBackButton()
            .setTitleRes(R.string.title_edit_profile)
            .setMenuRes(R.menu.edit_profile);
    }

    @Bind(R.id.current_full_name) TextView uiCurrentFullName;
    @Bind(R.id.current_email)     TextView uiCurrentEmail;
    @Bind(R.id.first_name)        EditText uiFirstName;
    @Bind(R.id.last_name)         EditText uiLastName;
    @Bind(R.id.email)             EditText uiEmail;

    UserInteractor userInteractor = DI.getInstance().getUserInteractor();

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        uiCurrentFullName.setText(userInteractor.getFirstName() + " " + userInteractor.getLastName());
        uiCurrentEmail.setText(userInteractor.getEmail());
        uiFirstName.setHint(userInteractor.getFirstName());
        uiLastName.setHint(userInteractor.getLastName());
        uiEmail.setHint(userInteractor.getEmail());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            userInteractor.updateUser(TextViews.textOf(uiFirstName), TextViews.textOf(uiLastName), TextViews.textOf(uiEmail))
                .compose(Rxs.doInBackgroundDeliverToUI())
                .subscribe(result -> {
                    Toast.makeText(this, "Profile has been updated", Toast.LENGTH_SHORT).show();
                }, ErrorUtils.handle(context()));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
