package es.guillermoorellana.keynotedex.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import es.guillermoorellana.keynotedex.presenter.ProfilePresenter
import es.guillermoorellana.keynotedex.presenter.ProfileView
import es.guillermoorellana.keynotedex.usecase.GetUserSessions

class ProfileActivity : AppCompatActivity() {

    private val view: ProfileView = object : ProfileView {

        private val textView: TextView
            get() = findViewById(R.id.mainText)

        override fun setText(text: String) = textView.setText(text)
    }

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        presenter = ProfilePresenter(view, GetUserSessions(keynotedexApplication.repository))
    }
}
