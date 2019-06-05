package es.guillermoorellana.keynotedex.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import es.guillermoorellana.keynotedex.presenter.ProfilePresenter
import es.guillermoorellana.keynotedex.presenter.ProfileView
import es.guillermoorellana.keynotedex.repository.model.Session
import es.guillermoorellana.keynotedex.repository.model.UserProfile

class ProfileActivity : AppCompatActivity() {

    private val view: ProfileView by lazy {
        object : ProfileView {

            private val listener: (Session) -> Unit = { session: Session -> presenter.onSessionClick(session) }

            private val sessionsAdapter = SessionsAdapter(listener)

            private val userName: TextView = findViewById(R.id.userName)
            private val userDescription: TextView = findViewById(R.id.userDescription)
            private val sessionsList: RecyclerView = findViewById<RecyclerView>(R.id.sessionsList).also {
                it.adapter = sessionsAdapter
                it.layoutManager = LinearLayoutManager(this@ProfileActivity)
            }

            override fun showProfile(profile: UserProfile) {
                userName.text = profile.user.displayName
                userDescription.text = profile.user.bio.takeUnless { it.isNullOrBlank() } ?: "No bio"
                sessionsAdapter.list = profile.sessions
            }

            override fun showLoading() {
                Snackbar.make(findViewById(R.id.rootView), "Loading", Snackbar.LENGTH_SHORT).show()
            }

            override fun showError(error: String) {
                Snackbar.make(findViewById(R.id.rootView), error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private lateinit var presenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        presenter = keynotedexApplication.notDagger.profilePresenter(view)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}

class SessionsAdapter(
    private val onSessionClick: (Session) -> Unit
) : RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {

    var list: List<Session> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return SessionViewHolder(view, onSessionClick)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) = holder.bind(list[position])

    class SessionViewHolder(
        view: View,
        val onSessionClick: (Session) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.sessionTitle)
        private val description = view.findViewById<TextView>(R.id.sessionDescription)
        private var clickListener: (View) -> Unit = {}

        init {
            view.setOnClickListener { clickListener(it) }
        }

        fun bind(session: Session) {
            title.text = session.title
            description.text = session.abstract
            clickListener = { onSessionClick(session) }
        }
    }
}
