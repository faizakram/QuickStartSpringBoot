
# 📌 SSH Key Setup for GitHub Access – Organization Enforces SSO

> **Short Description:**  
> Our organization has recently moved to the enterprise version of GitHub with Single Sign-On (SSO) enforcement. This guide provides clear steps to configure your SSH access correctly to avoid authentication issues while working with Git repositories.

---

## GitHub SSH Key Setup – Step-by-Step Guide

> **Note:** This guide has been prepared to assist in resolving the recent GitHub access issues we’ve encountered, particularly those related to SSH authentication. We hope it proves helpful in addressing these challenges smoothly.

## 🧩 Prerequisites

- Git must be installed on your system.  
- A GitHub account (https://github.com/)
- Terminal (Linux/macOS) or Git Bash (Windows)

---

## ✅ Step 1: Generate a New SSH Key

```bash
ssh-keygen -t ed25519 -C "your_email@example.com"
# Or if ed25519 is not supported
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```

Accept the default path or provide a custom one. Optionally, add a passphrase.

---

## 📂 Step 2: Start SSH Agent and Add the Key

```bash
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
```

---

## 📋 Step 3: Copy the Public Key

```bash
cat ~/.ssh/id_ed25519.pub
```

Copy the contents manually or use:

- Linux: `xclip -sel clip < ~/.ssh/id_ed25519.pub`
- macOS: `pbcopy < ~/.ssh/id_ed25519.pub`
- Windows (Git Bash): `clip < ~/.ssh/id_ed25519.pub`

---

## 🌐 Step 4: Add SSH Key to Your GitHub Account

1. Go to [GitHub](https://github.com)
2. Profile → Settings → SSH and GPG Keys → New SSH Key
3. Add a title and paste your key, then click **Add SSH Key**

---

## 🔒 Step 5: If Your Organization Enforces SSO

If your GitHub organization uses **SSO (Single Sign-On)** as part of the Enterprise plan, you must **authorize** your SSH key after adding it:

1. After adding the key, you'll see an option to **"Enable SSO"**
2. Click **Enable SSO** and select the appropriate organization

---

## 🧪 Step 6: Test SSH Connection

```bash
ssh -T git@github.com
```

If prompted, type `yes`. A successful message looks like:

```
Hi <your-username>! You've successfully authenticated, but GitHub does not provide shell access.
```

---

## 📌 Common Troubleshooting

| Error | Solution |
|------|----------|
| `Permission denied (publickey)` | Check if SSH key is added and SSO is enabled |
| `Could not resolve hostname` | Check your internet connection or proxy settings |
| `Repository not found` | Ensure you have access and the SSH URL is correct |

---

## ✅ Summary

| Step | Task |
|------|------|
| 1 | Generate SSH Key |
| 2 | Add key to SSH Agent |
| 3 | Copy public key |
| 4 | Add key to GitHub |
| 5 | Enable SSO if applicable |
| 6 | Test SSH connection |
