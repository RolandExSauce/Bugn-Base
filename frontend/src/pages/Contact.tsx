import { useRef, useState } from "react";
import { EMAIL_REGEX, NAME_REGEX, TEXT_REGEX } from "../utils/regex";
import MessageService from "../services/message.service";

const Contact = () => {
  const formRef = useRef<HTMLDivElement>(null);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    subject: "",
    message: "",
  });

  const [formInvalid, setFormInvalid] = useState({
    name: false,
    email: false,
    subject: false,
    message: false,
  });

  const handleChange = (key: keyof typeof formData, value: string) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
    // Clear validation error when user starts typing
    if (formInvalid[key]) {
      setFormInvalid((prev) => ({ ...prev, [key]: false }));
    }
  };

  const handleSubmit = async () => {
    const invalidFields = {
      name: !NAME_REGEX.test(formData.name),
      email: !EMAIL_REGEX.test(formData.email),
      subject: formData.subject.trim().length === 0,
      message: !TEXT_REGEX.test(formData.message),
    };

    setFormInvalid(invalidFields);

    const hasError = Object.values(invalidFields).some(Boolean);
    if (hasError) return;

    try {
      await MessageService.createMessage({
        name: formData.name,
        email: formData.email,
        subject: formData.subject,
        message: formData.message,
      });

      formRef.current?.classList.remove("success-animation");
      void formRef.current?.offsetWidth;
      formRef.current?.classList.add("success-animation");

      setTimeout(() => {
        setFormData({ name: "", email: "", subject: "", message: "" });
        setFormInvalid({
          name: false,
          email: false,
          subject: false,
          message: false,
        });
      }, 800);
    } catch (err) {
      console.error("Fehler beim Senden:", err);
    }
  };

  return (
    <div ref={formRef} className="d-flex flex-column container py-4">
      <h1 className="mb-4 text--primary">Kontaktiere uns</h1>

      <div className="d-flex flex-column flex-md-row gap-4">
        {/* Linke Spalte: Formular */}
        <div className="d-flex flex-column gap-3 flex-fill">
          {/* Name Feld */}
          <div>
            <label
              htmlFor="contact-name"
              className="form-label fw-semibold mb-1"
            >
              Dein Name <span className="text-danger">*</span>
            </label>
            <input
              id="contact-name"
              type="text"
              className="form-control"
              placeholder="z.B. Max Mustermann"
              value={formData.name}
              onChange={(e) => handleChange("name", e.target.value)}
            />
            {formInvalid.name && (
              <div className="text-danger small mt-1">Name is ungültig</div>
            )}
          </div>

          {/* Email Feld */}
          <div>
            <label
              htmlFor="contact-email"
              className="form-label fw-semibold mb-1"
            >
              E-Mail-Adresse <span className="text-danger">*</span>
            </label>
            <input
              id="contact-email"
              type="email"
              className="form-control"
              placeholder="name@example.com"
              value={formData.email}
              onChange={(e) => handleChange("email", e.target.value)}
            />
            {formInvalid.email && (
              <div className="text-danger small mt-1">
                Bitte gib eine gültige E-Mail-Adresse ein
              </div>
            )}
          </div>

          {/* Betreff Feld */}
          <div>
            <label
              htmlFor="contact-subject"
              className="form-label fw-semibold mb-1"
            >
              Betreff <span className="text-danger">*</span>
            </label>
            <input
              id="contact-subject"
              type="text"
              className="form-control"
              placeholder="Worum geht es?"
              value={formData.subject}
              onChange={(e) => handleChange("subject", e.target.value)}
            />
            {formInvalid.subject && (
              <div className="text-danger small mt-1">
                Bitte gib einen Betreff ein
              </div>
            )}
          </div>

          {/* Nachricht Feld */}
          <div>
            <label
              htmlFor="contact-message"
              className="form-label fw-semibold mb-1"
            >
              Nachricht <span className="text-danger">*</span>
            </label>
            <textarea
              id="contact-message"
              className="form-control"
              rows={4}
              placeholder="Schreibe hier deine Nachricht..."
              style={{ resize: "none", overflowY: "scroll" }}
              value={formData.message}
              onChange={(e) => handleChange("message", e.target.value)}
            />
            {formInvalid.message && (
              <div className="text-danger small mt-1">
                Nachricht darf nicht leer sein
              </div>
            )}
            <div className="text-muted small text-end mt-1">
              {formData.message.length} Zeichen
            </div>
          </div>

          <button
            className="btn btn-success align-self-start mt-2"
            onClick={handleSubmit}
            aria-label="Absenden"
          >
            Absenden
          </button>
        </div>

        {/* Rechte Spalte: Kontaktdaten */}
        <div className="d-flex flex-column gap-2 flex-fill border rounded p-3">
          <div className="fw-bold fs-5 mb-2">📞 Unsere Kontaktdaten</div>

          <div className="d-flex align-items-center gap-2">
            <span className="text-secondary">📍</span>
            <span>Blabla Straße 1</span>
          </div>

          <div className="d-flex align-items-center gap-2">
            <span className="text-secondary">📍</span>
            <span>12345 Stadt</span>
          </div>

          <div className="d-flex align-items-center gap-2">
            <span className="text-secondary">📱</span>
            <span>Telefon: 0123 456789</span>
          </div>

          <div className="d-flex align-items-center gap-2">
            <span className="text-secondary">✉️</span>
            <span>Email: info@example.com</span>
          </div>

          <div className="bg-light p-2 rounded mt-2 small text-muted">
            ⏰ Mo-Fr: 9-18 Uhr
          </div>
        </div>
      </div>
    </div>
  );
};

export default Contact;
