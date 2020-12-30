package edu.uw.nhyoung.election

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.towm1204.votedataprovider.StateResult
import com.towm1204.votedataprovider.VoteDataProvider
import kotlin.math.max
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get saved sort order
        val sortOrder: Int = savedInstanceState?.getInt("sort_order") ?: R.id.radio_state

        setContentView(R.layout.activity_main)

        // select radio button from saved state
        val radioGroup = findViewById<RadioGroup>(R.id.sort_categories)
        radioGroup.check(sortOrder)

        // get data and convert to mutable list, then sort based on selected radio button
        val electionResults: MutableList<StateResult> = VoteDataProvider.getAllResults().toMutableList()
        sortElection(sortOrder, electionResults)

        // calculate percentages
        val totalDem = electionResults.sumBy { it.demVote }
        val totalRep = electionResults.sumBy { it.repVote }
        val demPct = calculateRoundPercentage(totalDem, totalRep)
        val repPct = calculateRoundPercentage(totalRep, totalDem)

        // calculate overall winner
        val winner = if (demPct > repPct) {
            "Winner: Blue!"
        } else if (repPct > demPct){
            "Winner: Red!"
        } else {
            "Tie!"
        }

        // programmatically set text of banner view
        val winPct = max(demPct,repPct)
        val winnerView = findViewById<TextView>(R.id.text_view_winner)
        winnerView.text = winner
        val winnerStat = findViewById<TextView>(R.id.text_view_win_pct)
        winnerStat.text = "Winning with $winPct% of the vote"

        // initialize recycler view
        val adapter = ElectionAdapter(electionResults)
        val recycler = findViewById<RecyclerView>(R.id.recycler_list)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            sortElection(checkedId, electionResults)
            adapter.notifyDataSetChanged()
        }

    }

    // sorts the election based on the given category
    private fun sortElection(category: Int, electionResults: MutableList<StateResult>) {
        if (category == R.id.radio_state) {
            electionResults.sortBy { it.state }
        } else if (category == R.id.radio_bluest) {
            electionResults.sortBy { it.repVote.toDouble() / it.totalVote.toDouble() }
        } else {
            electionResults.sortBy { it.demVote.toDouble() / it.totalVote.toDouble() }
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("sort_order",findViewById<RadioGroup>(R.id.sort_categories).checkedRadioButtonId)
        super.onSaveInstanceState(savedInstanceState)
    }
}

class ElectionAdapter(private val theData: List<StateResult>) : RecyclerView.Adapter<ElectionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val result: View = view.findViewById<View>(R.id.result)
        val textView: TextView = view.findViewById<TextView>(R.id.txt_state)
        val blueBar: View = view.findViewById<View>(R.id.bar_blue)
        val redBar: View = view.findViewById<View>(R.id.bar_red)
        val bluePct: TextView = view.findViewById<TextView>(R.id.txt_blue_pct)
        val redPct: TextView = view.findViewById<TextView>(R.id.txt_red_pct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.state_result_list_item,
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theItem = theData[position]

        val percentDem: Int = calculateRoundPercentage(theItem.demVote, theItem.repVote)
        val percentRep: Int = calculateRoundPercentage(theItem.repVote, theItem.demVote)

        holder.blueBar.layoutParams =
            LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, percentRep.toFloat() / 100)
        holder.redBar.layoutParams =
            LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, percentDem.toFloat() / 100)
        holder.textView.text = theItem.state
        holder.bluePct.text = "$percentDem%"
        holder.redPct.text = "$percentRep%"
        holder.result.setOnClickListener{
            val toast = Toast.makeText(holder.result.context, "Blue: ${theItem.demVote}, Red: ${theItem.repVote}",Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun getItemCount() = theData.size
}

// calculates whole number percentage ratio of first integer to total
private fun calculateRoundPercentage(x: Int, y: Int) : Int {
    return round(x.toDouble() / (x.toDouble() + y.toDouble()) * 100).toInt()
}