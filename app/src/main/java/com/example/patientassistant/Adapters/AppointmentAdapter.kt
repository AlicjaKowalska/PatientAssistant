package com.example.patientassistant.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.patientassistant.Model.Appointment
import com.example.patientassistant.R

class AppointmentAdapter : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    var appointments: List<Appointment> = ArrayList()

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentHour: TextView = itemView.findViewById(R.id.appointmentHour)
        val appointmentDate: TextView = itemView.findViewById(R.id.appointmentDate)
        val appointmentTitle: TextView = itemView.findViewById(R.id.appointmentTitle)
        val appointmentFacility: TextView = itemView.findViewById(R.id.appointmentFacility)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.medical_appointment_item, parent, false)

        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {

        val currentAppointment: Appointment = appointments[position]

        with(holder) {
            appointmentHour.text =
                currentAppointment.hour.toString() + ":" + currentAppointment.minute.toString()
            appointmentDate.text =
                currentAppointment.day.toString() + "." + currentAppointment.month.toString() + "." + currentAppointment.year.toString()
            appointmentTitle.text = currentAppointment.title
            appointmentFacility.text = currentAppointment.facility
        }
    }

    override fun getItemCount() = appointments.size

    @JvmName("setAppointments1")
    fun setAppointments(myAppointments: List<Appointment>) {
        this.appointments = myAppointments
        notifyDataSetChanged()
    }
}