# ft_hangouts

**ft_hangouts** is a simple Android application developed as part of the 42 School. It serves as a basic contact management application, allowing users to add, view, and edit contacts.

## Features

- **Contact List:** View a list of saved contacts.
- **Add Contact:** Add a new contact with details such as name, phone number, and email.
- **View Contact:** View details of a specific contact.
- **Edit Contact:** Modify the details of an existing contact.
- **Delete Contact:** Remove a contact from the list.
- **Filter Contact:** Search contact.

### Bonus

- **Call Contact:** Option to call contact.
- **SMS Contact:** Option to send SMS to contact.

### Prerequisites

- Android Studio installed on your machine.
- A connected Android device or an emulator.

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/kingcreek/42-ft_hangouts.git
    ```

2. Open the project in Android Studio.

3. Build and run the project on your emulator or connected device.

### Test SMS

Install telnet client

connect: `telnet localhost <emulator_port>` (usually 5554, can check with `adb devices`)

auth: `auth <key>` (Located in `.emulator_console_auth_token` file, inside /home directory or user/<your_user>/)

send sms: `sms send <phone_number> <message>`
