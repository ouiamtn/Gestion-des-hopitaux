package ma.emsi.service;

import ma.emsi.entities.Consultation;
import ma.emsi.entities.Medecin;
import ma.emsi.entities.Patient;
import ma.emsi.entities.RendezVous;

public interface IHopitalService {
    Patient savePatient(Patient patient);
    Medecin saveMedecin(Medecin medecin);
    RendezVous saveRendezVous(RendezVous rendezVous);
    Consultation saveConsultation(Consultation consultation);
}
