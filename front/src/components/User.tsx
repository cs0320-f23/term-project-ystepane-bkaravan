export default function UserBox() {
  function submitForm() {
    // Get form elements
    const nameInput = document.getElementById("name");
    const phoneInput = document.getElementById("phone");
    const emailInput = document.getElementById("email");

    // Check if required fields are filled
    if (!nameInput.value || !phoneInput.value || !emailInput.value) {
      alert("Please fill in your information section");
      return;
    }
    //document.getElementById("origin-new").value = selected;
    var form = document.getElementById("myUserForm");
    var formData = new FormData(form);

    fetch("TODO", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        // Handle the response as needed
        console.log("Form submitted successfully:", response);
        // You can update the current page or perform other actions here
      })
      .catch((error) => {
        console.error("Error submitting form:", error);
      });
  }
  return (
    <div className="user-box">
      <form id="myUserForm">
        <div className="user-submit">
          <label htmlFor="user-info" className="user-info-container">
            Please enter your info:
          </label>
          <input
            type="text"
            id="name"
            name="name"
            placeholder="Enter your name"
            required
          />
          <input
            type="tel"
            id="phone"
            name="phone"
            placeholder="Enter phone number"
            required
          />
          <input
            type="email"
            id="email"
            name="email"
            placeholder="Enter email!"
            required
          />
          <br></br>
          <label htmlFor="smoking"> Smoking </label>
          <FormSelect thisID={"smoking"} />
          <label htmlFor="music"> Music </label>
          <FormSelect thisID={"music"} />
          <label htmlFor="talking"> Talking </label>
          <FormSelect thisID={"talking"} />
          <input
            type="button"
            value="Save my info!"
            onClick={() => submitForm()}
          />
        </div>
      </form>
    </div>
  );
}

const FormSelect = ({ thisID }) => {
  return (
    <select name="preferences" id={thisID}>
      <option selected disabled>
        Select Preference
      </option>
      <option value={5}>Highly preferred</option>
      <option value={3}>Moderately preferred</option>
      <option value={0}>Indifferent</option>
      <option value={-3}>Mildly avoided</option>
      <option value={-5}>Highly avoided</option>
    </select>
  );
};
