package com.example.burnestimation.data

import com.example.burnestimation.R
import com.example.burnestimation.datamodel.Patient

// TODO: read from local database
class Datasource() {

    fun loadPatients(): List<Patient> {
        return listOf<Patient>(
            Patient(
                "A5DR863FL4E3",
                "",
                74,
                160,
                "23 Nov 2021",
                "",
                "vicBurns",
                R.drawable.vicburns_16percent,
            ),
            Patient(
                "FJ0Y2S7HLQ42",
                "",
                74,
                160,
                "11 Nov 2021",
                "",
                "",
                R.drawable.burned_baby,
            ),
            Patient(
                "T4GH9UB0CPG9",
                "",
                74,
                160,
                "23 Nov 2021",
                "",
                "vicBurns",
                R.drawable.vicburns_16percent,
            )
        )
    }
}