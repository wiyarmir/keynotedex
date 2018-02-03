package es.guillermoorellana.keynotedex.web.model

data class Conference(val name: String)
data class User(val userId: String, val displayName: String, val submissions: List<Submission>)
data class Submission(val title: String, val abstract: String, val type: String, val submittedTo: String)
