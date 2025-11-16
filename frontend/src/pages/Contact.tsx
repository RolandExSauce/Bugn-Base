import { useRef, useState } from "react";
import { EMAIL_REGEX, NAME_REGEX, TEXT_REGEX } from "../types/regex";

const Contact = () => {
  const formRef = useRef<HTMLDivElement>(null);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });

  const [formInvalid, setFormInvalid] = useState({
    name: false,
    email: false,
    message: false,
  });

  const handleChange = (key: keyof typeof formData, value: string) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = () => {
    const nextInvalid = {
      name: !NAME_REGEX.test(formData.name),
      email: !EMAIL_REGEX.test(formData.email),
      message: !TEXT_REGEX.test(formData.message),
    };
    setFormInvalid(nextInvalid);

    const hasError = Object.values(nextInvalid).some(Boolean);
    if (hasError) return;

    formRef.current?.classList.remove("success-animation");
    void formRef.current?.offsetWidth;
    formRef.current?.classList.add("success-animation");

    setTimeout(() => {
      setFormData({ name: "", email: "", message: "" });
      setFormInvalid({ name: false, email: false, message: false });
    }, 800);
  };

  return (
    <div ref={formRef} className="d-flex flex-column container py-4">
      <h1 className="mb-4 text--primary">Kontaktiere uns</h1>

      <div className="d-flex flex-column flex-md-row gap-4">
        <div className="d-flex flex-column gap-3 flex-fill">
          <input
            type="text"
            className="form-control"
            placeholder="Dein Name"
            value={formData.name}
            onChange={(e) => handleChange("name", e.target.value)}
          />
          {formInvalid.name && (
            <div className="text-danger">Name darf nicht leer sein</div>
          )}

          <input
            type="text"
            className="form-control"
            placeholder="Email-Adresse"
            value={formData.email}
            onChange={(e) => handleChange("email", e.target.value)}
          />
          {formInvalid.email && (
            <div className="text-danger">Email ist ungültig</div>
          )}

          <textarea
            className="form-control"
            rows={4}
            placeholder="Nachricht"
            style={{ resize: "none", overflowY: "scroll" }}
            value={formData.message}
            onChange={(e) => handleChange("message", e.target.value)}
          />
          {formInvalid.message && (
            <div className="text-danger">Nachricht darf nicht leer sein</div>
          )}

          <button
            className="btn btn-success align-self-start"
            onClick={handleSubmit}
          >
            Absenden
          </button>
        </div>

        <div className="d-flex flex-column gap-2 flex-fill border rounded p-3">
          <div className="fw-bold">Unsere Kontaktdaten</div>
          <div>Blabla Straße 1</div>
          <div>12345 Stadt</div>
          <div>Telefon: 0123 456789</div>
          <div>Email: info@example.com</div>
        </div>
      </div>
    </div>
  );
};

export default Contact;
