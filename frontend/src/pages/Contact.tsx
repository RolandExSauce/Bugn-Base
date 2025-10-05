const Contact = () => {
  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4 text--primary">Kontaktiere uns</h1>

      <div className="d-flex flex-column flex-md-row gap-4">
        <div className="d-flex flex-column gap-3 flex-fill">
          <input type="text" className="form-control" placeholder="Dein Name" />
          <input
            type="email"
            className="form-control"
            placeholder="Email-Adresse"
          />
          <textarea
            className="form-control"
            rows={4}
            placeholder="Nachricht"
            style={{ resize: "none", overflowY: "scroll" }}
          ></textarea>
          <button className="btn btn-success align-self-start">Absenden</button>
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
